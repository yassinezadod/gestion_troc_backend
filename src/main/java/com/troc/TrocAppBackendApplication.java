package com.troc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.troc", "config"})
public class TrocAppBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrocAppBackendApplication.class, args);
	}

}
