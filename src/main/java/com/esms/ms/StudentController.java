package com.esms.ms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/student")
@RestController
public class StudentController {
	
	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}
	
	@GetMapping("/azure")
	public String indexAzure() {
		return "Greetings from Azure-Spring Boot!";
	}
	
	@GetMapping("/cloud")
	public String indexAzureCloud() {
		return "Greetings from Azure Cloud";
	}

}
