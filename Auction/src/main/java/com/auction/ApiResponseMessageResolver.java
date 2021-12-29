package com.auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

@Configuration
@PropertySources(value = {@PropertySource("classpath:/response_message.properties")})
public class ApiResponseMessageResolver {
	
	@Autowired
	private Environment environment;
	 
	public String getMessage(String property) {
		return this.environment.getProperty(property);
	}

}
