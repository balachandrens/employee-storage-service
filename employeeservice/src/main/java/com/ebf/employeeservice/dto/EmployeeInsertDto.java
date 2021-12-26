package com.ebf.employeeservice.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeInsertDto {
  @Valid
  @NotNull(message = "Employee cannot be null")
  private EmployeeInsertInfo employee;
}
