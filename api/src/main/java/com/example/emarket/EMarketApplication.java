package com.example.emarket;

import com.example.emarket.config.SecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class EMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(EMarketApplication.class, args);
	}

}
