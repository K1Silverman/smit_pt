package com.example.smit_pt;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.smit_pt.configClasses.QueryParam;
import com.example.smit_pt.controller.WorkshopController;
import com.example.smit_pt.dto.EndpointSettingsDto;
import com.example.smit_pt.dto.FilterSettingsDto;
import com.example.smit_pt.dto.WorkshopInfoDto;
import com.example.smit_pt.entity.FilterSettings;
import com.example.smit_pt.service.AvailableTimesService;
import com.example.smit_pt.service.BookingService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(WorkshopController.class)
@ExtendWith(ReadableTestWatcher.class)
public class WorkshopControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

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
		try {
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
		} catch (Exception e) {
			System.err.println("Test failed with exception: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}

	@Test
	public void testGetAllAvailableTimes() throws Exception {
		FilterSettings filterSettings = buildFilterSettings();
		List<WorkshopInfoDto> expectedResponse = buildWorkshopInfoDtoList();

		when(availableTimesService.getAvailableTimes(any(FilterSettings.class))).thenReturn(expectedResponse);
		try {
			mockMvc.perform(post("/available-time-filter")
					.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(filterSettings)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$", hasSize(1)))
					.andExpect(jsonPath("$[0].id").isNumber())
					.andExpect(jsonPath("$[0].name").isString())
					.andExpect(jsonPath("$[0].location").isString())
					.andExpect(jsonPath("$[0].vehicleTypes").isArray())
					.andExpect(jsonPath("$[0].availableTimes").isArray())
					.andExpect(jsonPath("$[0].currentPage").isNumber())
					.andExpect(jsonPath("$[0].lastPage").isNumber());

			verify(availableTimesService, times(1)).getAvailableTimes(any(FilterSettings.class));
		} catch (Exception e) {
			System.err.println("Test failed with exception: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	private FilterSettingsDto buildFilterSettingsDto() {
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

	private FilterSettings buildFilterSettings() {
		List<FilterSettingsDto> selectedWorkshops = Arrays.asList(buildFilterSettingsDto());

		FilterSettings filterSettings = FilterSettings.builder()
				.selectedWorkshops(selectedWorkshops)
				.pageSize(10)
				.pageNumber(1)
				.from(LocalDate.of(2024, 10, 02))
				.until(LocalDate.of(2024, 10, 02))
				.build();
		return filterSettings;

	}

	private List<WorkshopInfoDto> buildWorkshopInfoDtoList() {

		List<AvailableTime> availableTimes = new ArrayList<>();
		availableTimes.add(new AvailableTime(15, Instant.parse("2024-09-02T05:00:00Z"), true));
		availableTimes.add(new AvailableTime(16, Instant.parse("2024-09-02T06:00:00Z"), true));
		availableTimes.add(new AvailableTime(17, Instant.parse("2024-09-02T07:00:00Z"), true));
		availableTimes.add(new AvailableTime(18, Instant.parse("2024-09-02T08:00:00Z"), true));

		WorkshopInfoDto workshop1 = WorkshopInfoDto.builder().id(2).name("Manchester")
				.location("14 Bury New Rd, Manchester")
				.vehicleTypes(Arrays.asList("car", "truck")).availableTimes(availableTimes).currentPage(0).lastPage(0).build();

		return Arrays.asList(workshop1);
	}

	static public class AvailableTime {
		private Integer id;
		private Instant time;
		private boolean available;

		@JsonCreator
		public AvailableTime(
				@JsonProperty("id") Integer id,
				@JsonProperty("time") Instant time,
				@JsonProperty("available") boolean available) {

			this.id = id;
			this.time = time;
			this.available = available;
		}

		public Integer getId() {
			return id;
		}

		public Instant getTime() {
			return time;
		}

		public boolean isAvailable() {
			return available;
		}
	}
}

class ReadableTestWatcher implements TestWatcher {
	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		System.out.println("Test failed: " + context.getDisplayName());
		System.out.println("Cause: " + cause.getMessage());
		// You can add more detailed logging here
	}
}