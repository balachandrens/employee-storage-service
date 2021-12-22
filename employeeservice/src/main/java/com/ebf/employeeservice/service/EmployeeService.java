package com.ebf.employeeservice.service;

import com.ebf.employeeservice.dto.EmployeeLightResponseDto;
import com.ebf.employeeservice.dto.EmployeeResponseDto;
import com.ebf.employeeservice.dto.EmployeeUpsertDto;
import java.util.List;

public interface EmployeeService {

  EmployeeResponseDto save(long companyId, EmployeeUpsertDto employeeUpsertDto);
  EmployeeResponseDto findByCompanyIdAndEmployeeId(long companyId, long employeeId);
  List<EmployeeResponseDto> findAll();
  List<EmployeeLightResponseDto> findByCompanyId(long companyId);
  EmployeeResponseDto update(long companyId, long employeeId, EmployeeUpsertDto employeeUpsertDto);
  void removeEmployee(long companyId, long employeeId);
  Double findAverageSalary(long companyId);

}
