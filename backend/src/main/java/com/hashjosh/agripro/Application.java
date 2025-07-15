package com.hashjosh.agripro;

import com.hashjosh.agripro.config.dto.JwtProperties;
import com.hashjosh.agripro.config.dto.MailPropertiesDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({MailPropertiesDto.class, JwtProperties.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
