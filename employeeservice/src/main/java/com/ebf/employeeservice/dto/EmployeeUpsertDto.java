package com.ebf.employeeservice.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeUpsertDto {

  @NotEmpty(message = "Name cannot be empty")
  private String name;
  @NotEmpty(message = "Surname cannot be empty")
  private String surname;
  @Email(message = "Email should be a valid email id")
  private String email;
  private String address;
  @Positive(message = "Salary should be a valid value")
  private Double salary;

}
