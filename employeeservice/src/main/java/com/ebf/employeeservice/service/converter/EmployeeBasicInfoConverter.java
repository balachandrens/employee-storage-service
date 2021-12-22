package com.ebf.employeeservice.service.converter;

import com.ebf.employeeservice.dto.EmployeeLightResponseDto;
import com.ebf.employeeservice.dto.EmployeeResponseDto;
import com.ebf.employeeservice.dto.EmployeeUpsertDto;
import com.ebf.employeeservice.model.Employee;

public class EmployeeBasicInfoConverter {

  public static EmployeeLightResponseDto asEmployeeLightResponseDto(Employee employee) {
    return EmployeeLightResponseDto.builder().id(employee.getId()).name(employee.getName())
               .surname(employee.getSurname()).build();
  }

  public static EmployeeResponseDto asEmployeeResponseDto(Employee employee) {
    return EmployeeResponseDto.builder().id(employee.getId()).name(employee.getName()).surname(employee.getSurname())
               .address(employee.getAddress()).email(employee.getEmail()).salary(employee.getSalary()).build();
  }

  public static Employee asEmployeeModel(EmployeeUpsertDto employeeUpsertDto) {
    return Employee.builder().name(employeeUpsertDto.getName()).surname(employeeUpsertDto.getSurname())
               .address(employeeUpsertDto.getAddress()).email(employeeUpsertDto.getEmail())
               .salary(employeeUpsertDto.getSalary()).build();
  }


}
