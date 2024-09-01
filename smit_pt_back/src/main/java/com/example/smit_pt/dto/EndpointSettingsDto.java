package com.example.smit_pt.dto;

import java.util.List;

import com.example.smit_pt.configClasses.QueryParam;

import lombok.Data;

@Data
public class EndpointSettingsDto {
	private String desc;
	private List<QueryParam> queryParams;
}
