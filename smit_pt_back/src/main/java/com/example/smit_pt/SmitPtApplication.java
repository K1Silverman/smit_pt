package com.example.smit_pt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Your API Title", version = "1.0", description = "API description"))
public class SmitPtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmitPtApplication.class, args);
	}

}
