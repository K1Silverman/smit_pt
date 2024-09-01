package com.example.smit_pt.configClasses;

import java.util.List;

import lombok.Data;

@Data
public class EndpointSettings {
	private String desc;
	private String endpoint;
	private String method;
	private List<QueryParam> queryParams;
	private ResponseSettings response;
	private RequestSettings request;
}