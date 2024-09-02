package com.example.smit_pt.service;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.smit_pt.RestTemplateConfig;
import com.example.smit_pt.WorkshopsConfig;
import com.example.smit_pt.WorkshopsConfig.Workshop;
import com.example.smit_pt.configClasses.EndpointSettings;
import com.example.smit_pt.configClasses.QueryParam;
import com.example.smit_pt.dto.EndpointSettingsDto;
import com.example.smit_pt.dto.FilterSettingsDto;
import com.example.smit_pt.dto.WorkshopInfoDto;
import com.example.smit_pt.entity.FilterSettings;
import com.example.smit_pt.exception.QueryParamsException;
import com.example.smit_pt.mapper.EndpointSettingsMapper;
import com.example.smit_pt.mapper.FilterSettingsMapper;
import com.example.smit_pt.mapper.WorkshopInfoMapper;

import jakarta.annotation.Resource;

@Service
@Import(RestTemplateConfig.class)
public class AvailableTimesService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private WorkshopsConfig workshopsConfig;

	@Resource
	private WorkshopInfoMapper workshopInfoMapper;

	@Resource
	private FilterSettingsMapper filterSettingsMapper;

	@Resource
	private EndpointSettingsMapper endpointSettingsMapper;

	public List<WorkshopInfoDto> getAvailableTimes(FilterSettings filterSettings) {
		List<Workshop> filteredWorkshops = filterWorkshops(filterSettings.getSelectedWorkshops());

		List<WorkshopInfoDto> workshopInfoDtos = new ArrayList<>();
		for (Workshop workshop : filteredWorkshops) {

			WorkshopInfoDto workshopInfoDto = workshopInfoMapper.toDto(workshop);

			UriComponentsBuilder uriBuilder = buildUri(workshop, filterSettings);

			int pageSize = filterSettings.getPageSize();
			try {
				ResponseEntity<List<?>> objectResponse = restTemplate.exchange(uriBuilder.toUriString(),
						HttpMethod.GET, null, new ParameterizedTypeReference<List<?>>() {
						});
				if (objectResponse.hasBody() && objectResponse.getBody() != null) {

					List<Object> filteredBody = objectResponse.getBody().stream().filter(item -> {
						if (item instanceof Map map) {
							if (map.containsKey("available") && ((Boolean) map.get("available"))) {
								return isInDateRange((Map) item, filterSettings);
							} else
								return !map.containsKey("available");
						} else {
							throw new RuntimeException("Unexpected response format");
						}
					}).collect(Collectors.toList());
					int bodySize = filteredBody.size();
					int pageNumber = filterSettings.getPageNumber();
					int lastPage = (int) Math.ceil((double) bodySize / pageSize);
					workshopInfoDto.setLastPage(lastPage);
					workshopInfoDto.setCurrentPage(pageNumber);
					workshopInfoDto.setAvailableTimes(filteredBody);
				} else {
					workshopInfoDto.setAvailableTimes(Collections.emptyList());
				}
			} catch (RestClientException e) {
				workshopInfoDto.setAvailableTimes(Collections.emptyList());
			}

			workshopInfoDtos.add(workshopInfoDto);
		}
		return workshopInfoDtos;
	}

	public List<FilterSettingsDto> getWorkshopInfo() {

		List<Workshop> workshops = workshopsConfig.getWorkshops();

		List<FilterSettingsDto> filterSettingsDtos = new ArrayList<>();

		for (Workshop workshop : workshops) {
			FilterSettingsDto filterSettings = filterSettingsMapper.toDto(workshop);
			EndpointSettings getAction = workshop.getActions().stream()
					.filter(action -> action.getDesc().equals("getAvailable")).findFirst().get();
			List<EndpointSettingsDto> endpointSettingsDtos = new ArrayList<>();

			endpointSettingsDtos.add(endpointSettingsMapper.toDto(getAction));
			filterSettings.setEndpointSettings(endpointSettingsDtos);
			filterSettingsDtos.add(filterSettings);
		}

		return filterSettingsDtos;
	}

	public List<Workshop> filterWorkshops(List<FilterSettingsDto> selectedWorkshops) {
		List<Workshop> workshops = workshopsConfig.getWorkshops();
		if (selectedWorkshops.isEmpty()) {
			return workshops;
		} else {
			Set<Integer> selectedWorkshopIds = selectedWorkshops.stream().map(FilterSettingsDto::getId)
					.collect(Collectors.toSet());
			return workshops.stream().filter(workshop -> selectedWorkshopIds.contains(workshop.getId()))
					.toList();
		}
	}

	public UriComponentsBuilder buildUri(Workshop workshop, FilterSettings filterSettings) {
		EndpointSettings getAvailable = (EndpointSettings) workshop.getActions().stream()
				.filter(action -> action.getDesc().equals("getAvailable")).findFirst().orElse(null);

		String url = workshop.getApiUrl() + getAvailable.getEndpoint();

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url);
		for (QueryParam param : getAvailable.getQueryParams()) {
			for (Field field : filterSettings.getClass().getDeclaredFields()) {
				if (field.getName().equals(param.getName())) {
					field.setAccessible(true);
					try {
						uriBuilder.queryParam(param.getName(),
								(!Objects.isNull(field.get(filterSettings))
										&& (!field.get(filterSettings).toString().isEmpty()))
												? field.get(filterSettings)
												: param.getDefaultValue());
					} catch (IllegalAccessException e) {
						throw new QueryParamsException(e);
					}
				}
			}
		}
		return uriBuilder;
	}

	public boolean isInDateRange(Map item, FilterSettings filterSettings) {
		LocalDate time = Instant.parse(item.get("time").toString()).atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate from = filterSettings.getFrom();
		LocalDate until = filterSettings.getUntil();
		return time.isEqual(from) || (until != null && (time.isEqual(until) || time.isBefore(until)) || time.isAfter(from));
	}

}