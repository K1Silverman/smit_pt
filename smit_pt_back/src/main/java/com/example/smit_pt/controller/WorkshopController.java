package com.example.smit_pt.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.annotation.Resource;

@RestController
public class WorkshopController {

	@Autowired
	private WorkshopsConfig workshopsConfig;

	@Resource
	private AvailableTimesService availableTimesService;

	@Resource
	private BookingService bookingService;

	@GetMapping("/workshops")
	public List<FilterSettingsDto> getWorkshops() throws JsonMappingException, JsonProcessingException {
		List<FilterSettingsDto> response = availableTimesService.getWorkshopInfo();
		return response;
	}

	@PostMapping("/available-time-filter")
	public List<WorkshopInfoDto> getAllAvailableTimes(@RequestBody FilterSettings filterSettings) {
		List<WorkshopInfoDto> response = availableTimesService.getAvailableTimes(filterSettings);
		return response;
	}

	@PostMapping("/book-time")
	public Map<String, Object> bookTireChangeTime(@RequestBody Map<String, Object> bookingInformation) {
		Map<String, Object> response = bookingService.bookTireChangeTime(bookingInformation);
		return response;
	}
}
