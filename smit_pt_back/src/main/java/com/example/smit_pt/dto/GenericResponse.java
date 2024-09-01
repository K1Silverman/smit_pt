package com.example.smit_pt.dto;

import lombok.Data;

@Data
public class GenericResponse<T> {

	private T data;

	public GenericResponse(T data) {
		this.data = data;
	}

}
