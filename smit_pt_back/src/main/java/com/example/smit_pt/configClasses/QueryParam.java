package com.example.smit_pt.configClasses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryParam {
	private String name;
	private String type;
	private String defaultValue;
}