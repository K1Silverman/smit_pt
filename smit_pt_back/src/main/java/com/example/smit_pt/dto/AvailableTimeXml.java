package com.example.smit_pt.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "availableTime")
public class AvailableTimeXml {

	@XmlElement(name = "uuid")
	private String uuid;

	@XmlElement(name = "time")
	private String time;
}
