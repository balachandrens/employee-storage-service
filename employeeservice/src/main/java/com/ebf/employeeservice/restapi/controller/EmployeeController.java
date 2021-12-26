package com.ebf.employeeservice.restapi.controller;

import com.ebf.employeeservice.dto.EmployeeDetailsDto;
import com.ebf.employeeservice.dto.EmployeeInsertDto;
import com.ebf.employeeservice.dto.EmployeeList;
import com.ebf.employeeservice.dto.EmployeeUpdateDto;
import com.ebf.employeeservice.service.EmployeeService;
import com.ebf.employeeservice.util.ResponseBuilder;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/employees")
public class EmployeeController implements ResponseBuilder {

  private final EmployeeService employeeService;

  /**
   * Method to add employees for a company
   * @param employeeInsertDto
   * @return
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmployeeDetailsDto> addEmployee(@Valid @RequestBody EmployeeInsertDto employeeInsertDto) {
    return ResponseEntity.ok(new EmployeeDetailsDto(employeeService.save(employeeInsertDto.getEmployee())));
  }

  /**
   * Method to get all employees for a company
   * @param companyId
   * @return
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmployeeList> getAllEmployees(@RequestParam @NotEmpty(message = "Valid company Id is mandatory") long companyId) {
    return ResponseEntity.ok(new EmployeeList(employeeService.findByCompanyId(companyId)));
  }

  /**
   * Method to get employee details by id
   * @param employeeId
   * @return
   */
  @GetMapping(value = "/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmployeeDetailsDto> getEmployeeById(@PathVariable long employeeId) {
    return ResponseEntity.ok(new EmployeeDetailsDto(employeeService.findByEmployeeId(employeeId)));
  }

  /**
   * Method to update employee details
   * @param employeeId
   * @param employeeUpdateDto
   * @return
   */
  @PutMapping(value = "/{employeeId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<EmployeeDetailsDto> updateEmployeeData(@PathVariable long employeeId,
      @RequestBody @Valid EmployeeUpdateDto employeeUpdateDto) {
    return ResponseEntity.ok(new EmployeeDetailsDto(employeeService.update(employeeId, employeeUpdateDto.getEmployee())));
  }

  /**
   * Method to delete a employee record
   * @param employeeId
   * @return
   */
  @DeleteMapping(value = "/{employeeId}")
  public ResponseEntity<?> deleteEmployee(@PathVariable long employeeId) {
    employeeService.removeEmployee(employeeId);
    return ResponseEntity.noContent().build();
  }
}
