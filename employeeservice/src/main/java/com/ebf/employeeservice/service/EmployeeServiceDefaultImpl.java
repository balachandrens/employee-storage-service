package com.ebf.employeeservice.service;

import com.ebf.employeeservice.dto.EmployeeLightResponseDto;
import com.ebf.employeeservice.dto.EmployeeResponseDto;
import com.ebf.employeeservice.dto.EmployeeUpdateInfo;
import com.ebf.employeeservice.dto.EmployeeInsertInfo;
import com.ebf.employeeservice.exception.DataNotFoundException;
import com.ebf.employeeservice.exception.EmployeeServiceErrorCode;
import com.ebf.employeeservice.model.Company;
import com.ebf.employeeservice.model.Employee;
import com.ebf.employeeservice.repository.CompanyRepository;
import com.ebf.employeeservice.repository.EmployeeRepository;
import com.ebf.employeeservice.service.converter.EmployeeBasicInfoConverter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmployeeServiceDefaultImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final CompanyRepository companyRepository;

  public EmployeeServiceDefaultImpl(EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
    this.employeeRepository = employeeRepository;
    this.companyRepository = companyRepository;
  }

  @Override
  public EmployeeResponseDto save(EmployeeInsertInfo employeeInsertInfo) {
    Employee employee = EmployeeBasicInfoConverter.asEmployeeModel(employeeInsertInfo);
    Company company = companyRepository.findById(employeeInsertInfo.getCompanyId())
                          .orElseThrow(() -> new DataNotFoundException(EmployeeServiceErrorCode.COMPANY_NOT_FOUND));
    employee.setCompany(company);
    return EmployeeBasicInfoConverter.asEmployeeResponseDto(employeeRepository.save(employee));
  }

  @Override
  public EmployeeResponseDto findByEmployeeId(long employeeId) {
    Employee employee = employeeRepository.findById(employeeId).orElseThrow(
        () -> new DataNotFoundException(EmployeeServiceErrorCode.EMPLOYEE_NOT_FOUND));
    return EmployeeBasicInfoConverter.asEmployeeResponseDto(employee);
  }

  @Override
  public List<EmployeeResponseDto> findAll() {
    return employeeRepository.findAll().stream().map(EmployeeBasicInfoConverter::asEmployeeResponseDto)
               .collect(Collectors.toList());
  }

  @Override
  public List<EmployeeLightResponseDto> findByCompanyId(long companyId) {
    return employeeRepository.findAllByCompanyId(companyId).stream()
               .map(EmployeeBasicInfoConverter::asEmployeeLightResponseDto).collect(Collectors.toList());
  }

  @Override
  public EmployeeResponseDto update(long employeeId, EmployeeUpdateInfo employeeUpdateInfo) {
    Employee currentEmployee = employeeRepository.findById(employeeId).orElseThrow(
        () -> new DataNotFoundException(EmployeeServiceErrorCode.EMPLOYEE_NOT_FOUND));
    Employee updatedEmployee = EmployeeBasicInfoConverter.asEmployeeModel(employeeUpdateInfo);
    updatedEmployee.setId(currentEmployee.getId());
    updatedEmployee.setCompany(currentEmployee.getCompany());
    return EmployeeBasicInfoConverter.asEmployeeResponseDto(employeeRepository.save(updatedEmployee));
  }

  @Override
  public void removeEmployee(long employeeId) {
    try {
      employeeRepository.deleteById(employeeId);
    } catch (EmptyResultDataAccessException ex) {
      throw new DataNotFoundException(String.format("Employee with Id : %s not found", employeeId));
    }

  }

  @Override
  public Double findAverageSalary(long companyId) {
    return Objects.requireNonNullElse(employeeRepository.averageSalaryByCompanyId(companyId), 0.0);
  }

}
