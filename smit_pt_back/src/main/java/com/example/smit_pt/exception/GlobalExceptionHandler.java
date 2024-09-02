package com.example.smit_pt.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.smit_pt.exception.error.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleBookingException(BookingException e) {
		ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
		return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleQueryParamsException(QueryParamsException e) {
		ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
		errorResponse.setMessage(e.getMessage());
		errorResponse.setErrorCode(e.getErrorCode());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getAllErrors().stream()
				.map(ObjectError::getDefaultMessage)
				.collect(Collectors.joining(", "));

		ErrorResponse errorResponse = new ErrorResponse("VALIDATION_ERROR", errorMessage);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
}
