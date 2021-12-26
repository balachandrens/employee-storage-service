package com.ebf.employeeservice.service.converter;

import com.ebf.employeeservice.dto.EmployeeLightResponseDto;
import com.ebf.employeeservice.dto.EmployeeResponseDto;
import com.ebf.employeeservice.dto.EmployeeUpdateInfo;
import com.ebf.employeeservice.dto.EmployeeInsertInfo;
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

  public static Employee asEmployeeModel(EmployeeInsertInfo employeeInsertInfo) {
    return Employee.builder().name(employeeInsertInfo.getName()).surname(employeeInsertInfo.getSurname())
               .address(employeeInsertInfo.getAddress()).email(employeeInsertInfo.getEmail())
               .salary(employeeInsertInfo.getSalary()).build();
  }

  public static Employee asEmployeeModel(EmployeeUpdateInfo employeeUpdateInfo) {
    return Employee.builder().name(employeeUpdateInfo.getName()).surname(employeeUpdateInfo.getSurname())
               .address(employeeUpdateInfo.getAddress()).email(employeeUpdateInfo.getEmail())
               .salary(employeeUpdateInfo.getSalary()).build();
  }


}
