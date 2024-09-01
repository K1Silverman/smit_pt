package com.example.smit_pt.dto;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "tireChangeTimesResponse")
public class XmlResponse {

	@XmlElement(name = "availableTime")
	private List<AvailableTimeXml> availableTimes;

}