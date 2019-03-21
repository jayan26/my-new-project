package com.jayan.demo;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleSpringValidationApplication implements ApplicationRunner {

	public static final Logger logger = LogManager.getLogger(SampleSpringValidationApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SampleSpringValidationApplication.class, args);
		
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info("this is a info message");
	}

	
	
}

