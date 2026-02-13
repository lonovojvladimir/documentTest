package com.example.lonovojvladimir.documentTest;

import com.example.lonovojvladimir.documentTest.utility.DocumentGeneratorService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class
DocumentTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentTestApplication.class, args);
	}

	@Bean
	ApplicationRunner run(DocumentGeneratorService generator) {
		return args -> generator.generate();
	}

}
