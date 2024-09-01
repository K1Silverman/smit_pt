package com.example.smit_pt.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class QueryParamsException extends RuntimeException {
	private final HttpStatus statusCode;
	private final String errorCode;
	private final String errorMessage;

	public QueryParamsException(int statusCodeValue, String errorCode, String errorMessage) {
		super(errorMessage);
		this.statusCode = HttpStatus.valueOf(statusCodeValue);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public QueryParamsException(IllegalAccessException e) {
		super(e.getMessage());
		this.statusCode = HttpStatus.BAD_REQUEST;
		this.errorCode = "400";
		this.errorMessage = e.getMessage();
	}
}
