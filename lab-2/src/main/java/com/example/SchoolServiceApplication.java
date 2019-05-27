package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@RefreshScope
public class SchoolServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(SchoolServiceApplication.class, args);
	}

	@Autowired
	private Environment env;

	@PostMapping(value = "/actuator/bus-refresh", produces = "application/json; charset=UTF-8")
	public String checkRefresh() throws JsonProcessingException
	{
		return getProperties();
	}

	@GetMapping(value = "/properties", produces = "application/json; charset=UTF-8")
	public String getProperties() throws JsonProcessingException
	{
		Map<String, Object> props = new HashMap<>();
		CompositePropertySource bootstrapProperties = (CompositePropertySource)  ((AbstractEnvironment) env).getPropertySources().get("bootstrapProperties");
		for (String propertyName : bootstrapProperties.getPropertyNames()) {
			props.put(propertyName, bootstrapProperties.getProperty(propertyName));
		}
		return new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true).writerWithDefaultPrettyPrinter().writeValueAsString(props);
	}
}