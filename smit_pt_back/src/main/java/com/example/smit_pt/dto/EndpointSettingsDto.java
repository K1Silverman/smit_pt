package com.example.smit_pt.dto;

import java.util.List;

import com.example.smit_pt.configClasses.QueryParam;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EndpointSettingsDto {
	private String desc;
	private List<QueryParam> queryParams;
}
