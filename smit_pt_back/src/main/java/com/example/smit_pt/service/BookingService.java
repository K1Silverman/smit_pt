package com.example.smit_pt.service;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.smit_pt.RestTemplateConfig;
import com.example.smit_pt.WorkshopsConfig;
import com.example.smit_pt.WorkshopsConfig.Workshop;
import com.example.smit_pt.configClasses.EndpointSettings;
import com.example.smit_pt.exception.error.ErrorHandler;

import jakarta.annotation.Resource;

@Service
@Import(RestTemplateConfig.class)
public class BookingService {
	@Autowired
	private RestTemplate restTemplate;

	@Resource
	private WorkshopsConfig workshopsConfig;

	public Map<String, Object> bookTireChangeTime(Map<String, Object> bookingInformation) {
		List<Workshop> workshops = workshopsConfig.getWorkshops();

		for (Workshop workshop : workshops) {
			if (workshop.getId() == ((int) bookingInformation.get("workshopId"))) {
				EndpointSettings makeBooking = (EndpointSettings) workshop.getActions().stream()
						.filter(action -> action.getDesc().equals("makeBooking")).findFirst().orElse(null);

				UriComponentsBuilder uriBuilder = buildWorkshopUri(workshop, makeBooking);
				String requestMethod = makeBooking.getMethod();
				HttpMethod httpMethod;

				httpMethod = switch (requestMethod) {
					case "POST" -> HttpMethod.POST;
					case "PUT" -> HttpMethod.PUT;
					default -> HttpMethod.POST;
				};
				if (makeBooking.getRequest().getContentType().equals("xml")) {
					try {
						String xmlRequestBody = buildXmlRequestBody(bookingInformation, makeBooking);
						RequestEntity<Object> requestEntity = RequestEntity
								.method(httpMethod,
										URI.create(uriBuilder.buildAndExpand(bookingInformation.get("availableTimeId")).toUriString()))
								.contentType(MediaType.TEXT_XML).body(xmlRequestBody);

						ResponseEntity<Object> response = restTemplate.exchange(
								requestEntity, Object.class);
						return processResponse(response);
					} catch (HttpClientErrorException e) {
						ErrorHandler.handleHttpClientErrorException(e);
					}
				} else if (makeBooking.getRequest().getContentType().equals("json")) {
					try {
						Map<String, String> requestBodyMap = new HashMap<>();
						requestBodyMap.put("contactInformation", bookingInformation.get("contactInformation").toString());
						RequestEntity<Map<String, String>> requestEntity = RequestEntity
								.method(httpMethod,
										URI.create(uriBuilder.buildAndExpand(bookingInformation.get("availableTimeId")).toUriString()))
								.contentType(MediaType.APPLICATION_JSON).body(requestBodyMap);

						ResponseEntity<Object> response = restTemplate.exchange(
								requestEntity, Object.class);
						return processResponse(response);
					} catch (HttpClientErrorException e) {
						ErrorHandler.handleHttpClientErrorException(e);
					}
				}
			}
		}
		ResponseEntity<Object> response = ResponseEntity.status(404).build();
		return processResponse(response);
	}

	public UriComponentsBuilder buildWorkshopUri(Workshop workshop, EndpointSettings action) {

		String url = workshop.getApiUrl();
		String endpoint = action.getEndpoint();

		return UriComponentsBuilder
				.fromUriString(url + endpoint);
	}

	public String buildXmlRequestBody(Map<String, Object> bookingInformation, EndpointSettings action) {

		Document document = new Document();
		Element rootElement = new Element(action.getRequest().getRequestRoot());
		document.setRootElement(rootElement);

		Element contactInformationElement = new Element(action.getRequest().getRequestBody());
		contactInformationElement.setText(bookingInformation.get("contactInformation").toString());
		rootElement.addContent(contactInformationElement);

		XMLOutputter xmlOutputter = new XMLOutputter(Format.getCompactFormat());
		return xmlOutputter.outputString(document);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> processResponse(ResponseEntity<Object> response) {
		Object body = response.getBody();

		if (!(body instanceof Map)) {
			throw new RuntimeException("Unexpected response format");
		}

		Map<String, Object> responseMap = (Map<String, Object>) body;

		if (responseMap.containsKey("available") && !(Boolean) responseMap.get("available")) {
			responseMap.remove("available");
		}

		return responseMap;
	}
}
