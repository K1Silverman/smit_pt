package com.example.smit_pt.exception.error;

import lombok.Data;

@Data
public class ErrorResponse {

	private String errorCode;
	private String message;

	public ErrorResponse(String errorCode, String message) {

		this.errorCode = errorCode;
		this.message = message;
	}
}
