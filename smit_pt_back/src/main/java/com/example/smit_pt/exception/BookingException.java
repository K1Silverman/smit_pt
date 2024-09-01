package com.example.smit_pt.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class BookingException extends RuntimeException {
	private final HttpStatus statusCode;
	private final String errorCode;
	private final String errorMessage;

	public BookingException(int statusCodeValue, String errorCode, String errorMessage) {
		super(String.format("HTTP %d - %s: %s", statusCodeValue, errorCode, errorMessage));
		this.statusCode = HttpStatus.valueOf(statusCodeValue);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}
