package com.example.smit_pt.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkshopInfoDto {
	private Integer id;
	private String name;
	private String location;
	private List<String> vehicleTypes;
	private List<?> availableTimes;
	private Integer currentPage;
	private Integer lastPage;
}
