package com.example.smit_pt.entity;

import java.util.List;

import com.example.smit_pt.dto.FilterSettingsDto;

import lombok.Data;

@Data
public class FilterSettings {

	public List<FilterSettingsDto> selectedWorkshops;
	public Integer pageSize;
	public Integer pageNumber;
	public String from;
	public String until;
}
