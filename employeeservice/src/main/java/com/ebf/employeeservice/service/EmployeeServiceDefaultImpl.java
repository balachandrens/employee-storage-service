package com.ebf.employeeservice.service;

import com.ebf.employeeservice.dto.EmployeeLightResponseDto;
import com.ebf.employeeservice.dto.EmployeeResponseDto;
import com.ebf.employeeservice.dto.EmployeeUpsertDto;
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
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmployeeServiceDefaultImpl implements EmployeeService {

  private final EmployeeBasicInfoConverter employeeBasicInfoConverter;
  private final EmployeeRepository employeeRepository;
  private final CompanyRepository companyRepository;

  public EmployeeServiceDefaultImpl(EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
    this.employeeRepository = employeeRepository;
    this.companyRepository = companyRepository;
    this.employeeBasicInfoConverter = new EmployeeBasicInfoConverter();
  }

  @Override
  public EmployeeResponseDto save(long companyId, EmployeeUpsertDto employeeUpsertDto) {
    Employee employee = employeeBasicInfoConverter.asEmployeeModel(employeeUpsertDto);
    Company company = companyRepository.findById(companyId)
                          .orElseThrow(() -> new DataNotFoundException(EmployeeServiceErrorCode.COMPANY_NOT_FOUND));
    employee.setCompany(company);
    return employeeBasicInfoConverter.asEmployeeResponseDto(employeeRepository.save(employee));
  }

  @Override
  public EmployeeResponseDto findByCompanyIdAndEmployeeId(long companyId, long employeeId) {
    Employee employee = employeeRepository.findByIdAndCompanyId(employeeId, companyId).orElseThrow(
        () -> new DataNotFoundException(EmployeeServiceErrorCode.EMPLOYEE_NOT_FOUND_FOR_COMPANY));
    return employeeBasicInfoConverter.asEmployeeResponseDto(employee);
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
  public EmployeeResponseDto update(long companyId, long employeeId, EmployeeUpsertDto employeeUpsertDto) {
    companyRepository.findById(companyId)
        .orElseThrow(() -> new DataNotFoundException(EmployeeServiceErrorCode.COMPANY_NOT_FOUND));
    Employee currentEmployee = employeeRepository.findByIdAndCompanyId(employeeId, companyId).orElseThrow(
        () -> new DataNotFoundException(EmployeeServiceErrorCode.EMPLOYEE_NOT_FOUND_FOR_COMPANY));
    Employee updatedEmployee = employeeBasicInfoConverter.asEmployeeModel(employeeUpsertDto);
    updatedEmployee.setId(currentEmployee.getId());
    updatedEmployee.setCompany(currentEmployee.getCompany());
    return employeeBasicInfoConverter.asEmployeeResponseDto(employeeRepository.save(updatedEmployee));
  }

  @Override
  public void removeEmployee(long companyId, long employeeId) {
    employeeRepository.deleteByIdAndCompanyId(employeeId, companyId);
  }

  @Override
  public Double findAverageSalary(long companyId) {
    return Objects.requireNonNullElse(employeeRepository.averageSalaryByCompanyId(companyId), 0.0);
  }

}
