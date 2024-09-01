package com.example.smit_pt.exception.error;

import org.springframework.web.client.HttpClientErrorException;

import com.example.smit_pt.exception.BookingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ErrorHandler {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static void handleHttpClientErrorException(HttpClientErrorException ex) throws BookingException {
		String responseBody = ex.getResponseBodyAsString();
		String errorCode = "UNKNOWN";
		String errorMessage = responseBody;

		try {
			JsonNode jsonNode = objectMapper.readTree(responseBody);

			if (jsonNode.has("code")) {
				errorCode = jsonNode.get("code").asText();
			}
			if (jsonNode.has("message")) {
				errorMessage = jsonNode.get("message").asText();
				errorMessage = errorMessage.substring(0, 1).toUpperCase() + errorMessage.substring(1);
			}
		} catch (Exception e) {
			errorCode = "EXTERNAL_API_ERROR";
		}

		throw new BookingException(ex.getStatusCode().value(), errorCode, errorMessage);
	}
}