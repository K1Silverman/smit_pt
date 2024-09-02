package com.example.smit_pt.entity;

import java.time.LocalDate;
import java.util.List;

import com.example.smit_pt.dto.FilterSettingsDto;
import com.example.smit_pt.validator.ValidDateRange;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ValidDateRange
public class FilterSettings {

	public List<FilterSettingsDto> selectedWorkshops;
	public Integer pageSize;
	public Integer pageNumber;
	@NotNull(message = "From cannot be null")
	public LocalDate from;
	public LocalDate until;
}
