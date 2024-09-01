package com.example.smit_pt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.example.smit_pt.configClasses.EndpointSettings;

import jakarta.annotation.PostConstruct;
import lombok.Data;

@Configuration
@PropertySource("classpath:workshops.properties")
@ConfigurationProperties("workshops")
@Component
@Data
public class WorkshopsConfig {
	private List<Workshop> workshops = new ArrayList<Workshop>();

	@Data
	public static class Workshop {
		private int id;
		private String name;
		private String location;
		private List<String> vehicleTypes;
		private String apiUrl;
		private List<EndpointSettings> actions;

	}
}