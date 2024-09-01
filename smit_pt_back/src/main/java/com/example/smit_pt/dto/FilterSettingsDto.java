package com.example.smit_pt.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilterSettingsDto {
	private Integer id;
	private String name;
	private String location;
	private List<String> vehicleTypes;
	@Builder.Default
	private List<EndpointSettingsDto> endpointSettings = new ArrayList<>();

}
