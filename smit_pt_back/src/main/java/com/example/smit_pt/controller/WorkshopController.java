package com.example.smit_pt.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.smit_pt.WorkshopsConfig;
import com.example.smit_pt.dto.FilterSettingsDto;
import com.example.smit_pt.dto.WorkshopInfoDto;
import com.example.smit_pt.entity.FilterSettings;
import com.example.smit_pt.service.AvailableTimesService;
import com.example.smit_pt.service.BookingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@Validated
public class WorkshopController {

	@Autowired
	private WorkshopsConfig workshopsConfig;

	@Resource
	private AvailableTimesService availableTimesService;

	@Resource
	private BookingService bookingService;

	@GetMapping("/workshops")
	@Operation(summary = "Get all available workshops", description = "Fetches all available workshops from workshop configurations")
	public List<FilterSettingsDto> getWorkshops() throws JsonMappingException, JsonProcessingException {
		List<FilterSettingsDto> response = availableTimesService.getWorkshopInfo();
		return response;
	}

	@PostMapping("/available-time-filter")
	@Operation(summary = "Get available times with filter settings", description = "Fetches available times for workshops that fit the filter conditions")
	public List<WorkshopInfoDto> getAllAvailableTimes(@Valid @RequestBody FilterSettings filterSettings) {
		List<WorkshopInfoDto> response = availableTimesService.getAvailableTimes(filterSettings);
		return response;
	}

	@PostMapping("/book-time")
	@Operation(summary = "Book time for workshop", description = "Books time for workshop if it is available")
	public Map<String, Object> bookTireChangeTime(@RequestBody Map<String, Object> bookingInformation) {
		Map<String, Object> response = bookingService.bookTireChangeTime(bookingInformation);
		return response;
	}
}
