package com.ebf.employeeservice.service;

import com.ebf.employeeservice.dto.EmployeeInsertInfo;
import com.ebf.employeeservice.dto.EmployeeLightResponseDto;
import com.ebf.employeeservice.dto.EmployeeResponseDto;
import com.ebf.employeeservice.dto.EmployeeUpdateInfo;
import java.util.List;

public interface EmployeeService {

  EmployeeResponseDto save(EmployeeInsertInfo employeeInsertInfo);
  EmployeeResponseDto findByEmployeeId(long employeeId);
  List<EmployeeResponseDto> findAll();
  List<EmployeeLightResponseDto> findByCompanyId(long companyId);
  EmployeeResponseDto update(long employeeId, EmployeeUpdateInfo employeeUpdateInfo);
  void removeEmployee(long employeeId);
  Double findAverageSalary(long companyId);

}
