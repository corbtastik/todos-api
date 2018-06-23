package io.corbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
public class TodosApp {

	public static void main(String[] args) {
		SpringApplication.run(TodosApp.class, args);
	}
}
