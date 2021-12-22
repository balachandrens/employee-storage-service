package com.ebf.employeeservice;

import com.ebf.employeeservice.model.Company;
import com.ebf.employeeservice.repository.CompanyRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EmployeeServiceApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}

	/**
	 * This bean is to load the company data from json file
	 * @param companyRepository
	 */
	@Bean
	CommandLineRunner runner(CompanyRepository companyRepository){
		return args -> {
			TypeReference<List<Company>> typeReference = new TypeReference<List<Company>>(){};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/static/company-data.json");
			try {
				List<Company> companyList = new ObjectMapper().readValue(inputStream,typeReference);
				companyRepository.saveAll(companyList);
			} catch (IOException e){
				LOGGER.error("Unable to save data: " + e.getMessage());
			}
		};
	}

}
