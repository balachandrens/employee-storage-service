package com.ebf.employeeservice.dto;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeUpdateDto {
  @Valid
  private EmployeeUpdateInfo employee;
}
