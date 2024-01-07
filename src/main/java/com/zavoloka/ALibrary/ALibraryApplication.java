package com.zavoloka.ALibrary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication
@EntityScan(basePackages = "com.zavoloka.ALibrary.model")
@EnableJpaRepositories(basePackages = "com.zavoloka.ALibrary.repository")
public class ALibraryApplication {

	private static final Logger logger = LoggerFactory.getLogger(ALibraryApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ALibraryApplication.class, args);
	}


}
