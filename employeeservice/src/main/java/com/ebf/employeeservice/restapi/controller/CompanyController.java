package com.ebf.employeeservice.restapi.controller;

import com.ebf.employeeservice.dto.CompanyAvgSalaryDto;
import com.ebf.employeeservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

  private final EmployeeService employeeService;

  /**
   * Method to fetch average salary of employees of a company
   * @param companyId
   * @return
   */
  @GetMapping(value = "/{companyId}/average-salary", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CompanyAvgSalaryDto> getAverageSalary(@PathVariable long companyId) {
    return ResponseEntity
               .ok(CompanyAvgSalaryDto.builder().averageSalary(employeeService.findAverageSalary(companyId)).build());
  }

}
