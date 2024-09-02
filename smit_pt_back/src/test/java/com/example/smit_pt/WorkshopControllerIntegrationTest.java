package com.example.smit_pt;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.smit_pt.configClasses.QueryParam;
import com.example.smit_pt.controller.WorkshopController;
import com.example.smit_pt.dto.EndpointSettingsDto;
import com.example.smit_pt.dto.FilterSettingsDto;
import com.example.smit_pt.service.AvailableTimesService;
import com.example.smit_pt.service.BookingService;

@WebMvcTest(WorkshopController.class)
public class WorkshopControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AvailableTimesService availableTimesService;

	@MockBean
	private BookingService bookingService;

	@MockBean
	private WorkshopsConfig workshopsConfig;

	@Test
	public void testGetWorkshops() throws Exception {
		FilterSettingsDto workshop1 = buildFilterSettingsDto();
		List<FilterSettingsDto> expectedWorkshops = Arrays.asList(workshop1);

		when(availableTimesService.getWorkshopInfo()).thenReturn(expectedWorkshops);

		mockMvc.perform(get("/workshops")).andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$", hasSize(expectedWorkshops.size())))
				.andExpect(jsonPath("$[0].id").value(2))
				.andExpect(jsonPath("$[0].name").value("Manchester"))
				.andExpect(jsonPath("$[0].location").value("14 Bury New Rd, Manchester"))
				.andExpect(jsonPath("$[0].vehicleTypes").isArray())
				.andExpect(jsonPath("$[0].vehicleTypes", hasItems("car", "truck")))
				.andExpect(jsonPath("$[0].endpointSettings").isArray())
				.andExpect(jsonPath("$[0].endpointSettings[0].queryParams").isArray())
				.andExpect(jsonPath("$[0].endpointSettings[0].queryParams.length()").value(3));

		verify(availableTimesService, times(1)).getWorkshopInfo();
	}

	public static FilterSettingsDto buildFilterSettingsDto() {
		QueryParam amountParam = QueryParam.builder()
				.name("amount")
				.type("integer")
				.build();

		QueryParam pageParam = QueryParam.builder()
				.name("page")
				.type("integer")
				.build();

		QueryParam fromParam = QueryParam.builder()
				.name("from")
				.type("date")
				.defaultValue("2006-01-02")
				.build();

		List<QueryParam> queryParams = Arrays.asList(amountParam, pageParam, fromParam);

		EndpointSettingsDto endpointSettings = EndpointSettingsDto.builder().desc("getAvailable").queryParams(queryParams)
				.build();

		return FilterSettingsDto.builder()
				.id(2)
				.name("Manchester")
				.location("14 Bury New Rd, Manchester")
				.vehicleTypes(Arrays.asList("car", "truck"))
				.endpointSettings(Arrays.asList(endpointSettings))
				.build();
	}

}
