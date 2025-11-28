package com.bruno.reservo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bruno.reservo")
public class ReservoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservoApplication.class, args);
	}

}
