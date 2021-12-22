package com.ebf.employeeservice.restapi.controller;

import com.ebf.employeeservice.dto.CompanyAvgSalaryDto;
import com.ebf.employeeservice.dto.EmployeeLightResponseDto;
import com.ebf.employeeservice.dto.EmployeeResponseDto;
import com.ebf.employeeservice.dto.EmployeeUpsertDto;
import com.ebf.employeeservice.service.EmployeeService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {

  private final EmployeeService employeeService;

  @RequestMapping(value = "/{companyId}/employees", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmployeeResponseDto> addEmployee(@PathVariable long companyId,
      @Valid @RequestBody EmployeeUpsertDto employeeUpsertDto) {
    return ResponseEntity.ok(employeeService.save(companyId, employeeUpsertDto));
  }

  @GetMapping(value = "/{companyId}/employees")
  public ResponseEntity<List<EmployeeLightResponseDto>> getAllEmployees(@PathVariable long companyId) {
    return ResponseEntity.ok(employeeService.findByCompanyId(companyId));
  }

  @GetMapping(value = "/{companyId}/employees/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable long companyId,
      @PathVariable long employeeId) {
    return ResponseEntity.ok(employeeService.findByCompanyIdAndEmployeeId(companyId, employeeId));
  }

  @PutMapping(value = "/{companyId}/employees/{employeeId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmployeeResponseDto> updateEmployeeData(@PathVariable long companyId,
      @PathVariable long employeeId, @RequestBody EmployeeUpsertDto employeeUpsertDto) {
    return ResponseEntity.ok(employeeService.update(companyId, employeeId, employeeUpsertDto));
  }

  @DeleteMapping(value = "/{companyId}/employees/{employeeId}")
  public ResponseEntity<?> deleteEmployee(@PathVariable long companyId, @PathVariable long employeeId) {
    employeeService.removeEmployee(companyId, employeeId);
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/{companyId}/average-salary", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CompanyAvgSalaryDto> getAverageSalary(@PathVariable long companyId) {
    return ResponseEntity
               .ok(CompanyAvgSalaryDto.builder().averageSalary(employeeService.findAverageSalary(companyId)).build());
  }

}
