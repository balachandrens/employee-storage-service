package com.ebf.employeeservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ebf.employeeservice.dto.EmployeeInsertInfo;
import com.ebf.employeeservice.dto.EmployeeUpdateInfo;
import com.ebf.employeeservice.exception.DataNotFoundException;
import com.ebf.employeeservice.exception.EmployeeServiceErrorCode;
import com.ebf.employeeservice.model.Company;
import com.ebf.employeeservice.model.Employee;
import com.ebf.employeeservice.repository.CompanyRepository;
import com.ebf.employeeservice.repository.EmployeeRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceDefaultImplTests {

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private CompanyRepository companyRepository;

  private EmployeeServiceDefaultImpl employeeService;


  private static final String NAME_PREFIX = "Employee_";
  private static final String DEFAULT_ADDRESS = "Sample address";
  private static final String EMAIL_SUFFIX = "@mailinator.com";
  private static final long EMPLOYEE_1_ID = 35716653238686L;
  private static final long COMPANY_1_ID = 45716653238686L;
  private static final String COMPANY_1_NAME = "Company1";
  private static final long EMPLOYEE_2_ID = 36716653238686L;
  private static final long COMPANY_2_ID = 46716653238686L;
  private static final String COMPANY_2_NAME = "Company2";

  @BeforeEach
  void before() {
    employeeService = new EmployeeServiceDefaultImpl(employeeRepository, companyRepository);
  }

  @Test
  public void shouldDelegateCreation() {
    //given
    Company company = createCompanyObject(COMPANY_1_ID, COMPANY_1_NAME);
    Employee employee = createEmployeeObject("_1", 8000.0, company);
    var savedEmployee = getEmployeeCloneWithId(EMPLOYEE_1_ID, employee);
    EmployeeInsertInfo employeeInsertDto = convertToEmployeeInsertDto(employee);
    when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);
    when(companyRepository.findById(COMPANY_1_ID)).thenReturn(Optional.ofNullable(company));
    //when
    var employeeDtoResponse = employeeService.save(employeeInsertDto);
    //then
    verify(companyRepository).findById(COMPANY_1_ID);
    verify(employeeRepository).save(any(Employee.class));
    assertThat(employeeDtoResponse.getName()).isEqualTo(employeeInsertDto.getName());
  }

  @Test
  public void shouldDelegateCreation_throwsDataNotFoundException() {
    //given
    Company company = createCompanyObject(COMPANY_1_ID, COMPANY_1_NAME);
    Employee employee = createEmployeeObject("_1", 8000.0, company);
    var savedEmployee = getEmployeeCloneWithId(EMPLOYEE_1_ID, employee);
    EmployeeInsertInfo employeeInsertDto = convertToEmployeeInsertDto(employee);
    when(companyRepository.findById(COMPANY_1_ID)).thenReturn(Optional.empty());
    //when & then
    DataNotFoundException thrown = Assertions.assertThrows(DataNotFoundException.class, () -> {
      var employeeDtoResponse = employeeService.save(employeeInsertDto);
    });

    //then
    verify(companyRepository).findById(COMPANY_1_ID);
    assertThat(thrown.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(thrown.getMessage()).isEqualTo(EmployeeServiceErrorCode.COMPANY_NOT_FOUND.getMessage());
  }

  @Test
  public void shouldFindAllEmployeesForCompany() {
    //given
    Company company1 = createCompanyObject(COMPANY_1_ID, COMPANY_1_NAME);
    Company company2 = createCompanyObject(COMPANY_2_ID, COMPANY_2_NAME);
    Employee employee1 = createEmployeeObjectWithId(EMPLOYEE_1_ID, "_1", 8000.0, company1);
    Employee employee2 = createEmployeeObjectWithId(EMPLOYEE_2_ID, "_2", 8000.0, company2);
    when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));
    //when
    var employeeDtoList = employeeService.findAll();
    //then
    verify(employeeRepository).findAll();
    assertThat(employeeDtoList.size()).isEqualTo(2);
    assertThat(employeeDtoList.get(0).getId()).isIn(EMPLOYEE_1_ID, EMPLOYEE_2_ID);
    assertThat(employeeDtoList.get(1).getId()).isIn(EMPLOYEE_1_ID, EMPLOYEE_2_ID);
  }

  @Test
  public void shouldFindAllEmployeesByCompanyId() {
    //given
    Company company1 = createCompanyObject(COMPANY_1_ID, COMPANY_1_NAME);
    Employee employee1 = createEmployeeObjectWithId(EMPLOYEE_1_ID, "_1", 8000.0, company1);
    Employee employee2 = createEmployeeObjectWithId(EMPLOYEE_2_ID, "_2", 8000.0, company1);
    when(employeeRepository.findAllByCompanyId(COMPANY_1_ID)).thenReturn(Arrays.asList(employee1, employee2));
    //when
    var employeeDtoList = employeeService.findByCompanyId(COMPANY_1_ID);
    //then
    verify(employeeRepository).findAllByCompanyId(COMPANY_1_ID);
    assertThat(employeeDtoList.size()).isEqualTo(2);
    assertThat(employeeDtoList.get(1).getId()).isIn(EMPLOYEE_1_ID, EMPLOYEE_2_ID);
  }

  @Test
  public void shouldGetEmptyListOfEmployeesByCompanyID() {
    //given
    when(employeeRepository.findAllByCompanyId(COMPANY_1_ID)).thenReturn(Collections.emptyList());
    //when
    var employeeDtoList = employeeService.findByCompanyId(COMPANY_1_ID);
    //then
    verify(employeeRepository).findAllByCompanyId(COMPANY_1_ID);
    assertThat(employeeDtoList.size()).isEqualTo(0);
  }

  @Test
  public void shouldFindEmployeeById() {
    //given
    Company company = createCompanyObject(COMPANY_1_ID, COMPANY_1_NAME);
    Employee employee = createEmployeeObjectWithId(EMPLOYEE_1_ID, "_1", 90000.0, company);
    when(employeeRepository.findById(EMPLOYEE_1_ID)).thenReturn(Optional.of(employee));
    //when
    var savedEmployee = employeeService.findByEmployeeId(EMPLOYEE_1_ID);
    //then
    verify(employeeRepository).findById(EMPLOYEE_1_ID);
    assertThat(savedEmployee.getName()).isEqualTo(employee.getName());
    assertThat(savedEmployee.getId()).isEqualTo(EMPLOYEE_1_ID);
  }

  @Test
  public void shouldFindEmployeeById_throwsDataNotFoundException() {
    //given
    when(employeeRepository.findById(EMPLOYEE_1_ID)).thenReturn(Optional.empty());
    //when & then
    DataNotFoundException thrown = Assertions.assertThrows(DataNotFoundException.class, () -> {
      var savedEmployee = employeeService.findByEmployeeId(EMPLOYEE_1_ID);
    });

    //then
    verify(employeeRepository).findById(EMPLOYEE_1_ID);
    assertThat(thrown.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(thrown.getMessage()).isEqualTo(EmployeeServiceErrorCode.EMPLOYEE_NOT_FOUND.getMessage());
  }

  @Test
  public void shouldDelegateEmployeeUpdate_NameAndAddress() {
    //given
    Company company = createCompanyObject(COMPANY_1_ID, COMPANY_1_NAME);
    Employee savedEmployee = createEmployeeObjectWithId(EMPLOYEE_1_ID, "_1", 8000.0, company);
    EmployeeUpdateInfo employeeUpdateInfo = convertToEmployeeUpdateDto(savedEmployee);
    employeeUpdateInfo.setName("John Doe");
    employeeUpdateInfo.setAddress("Brite Str, 38, 50670");
    Employee updatedEmployee = getEmployeeCloneWithId(savedEmployee.getId(), savedEmployee);
    updatedEmployee.setName("John Doe");
    updatedEmployee.setAddress("Brite Str, 38, 50670");
    when(employeeRepository.findById(EMPLOYEE_1_ID)).thenReturn(Optional.of(savedEmployee));
    when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);
    //when
    var employeeDtoResponse = employeeService.update(EMPLOYEE_1_ID, employeeUpdateInfo);
    //then
    verify(employeeRepository).findById(EMPLOYEE_1_ID);
    verify(employeeRepository).save(any(Employee.class));
    assertThat(employeeDtoResponse.getName()).isEqualTo(employeeUpdateInfo.getName());
    assertThat(employeeDtoResponse.getAddress()).isEqualTo(employeeUpdateInfo.getAddress());
  }

  @Test
  public void shouldDelegateEmployeeUpdate_throwsEmployeeDataNotFoundException() {
    //given
    Company company = createCompanyObject(COMPANY_1_ID, COMPANY_1_NAME);
    Employee savedEmployee = createEmployeeObjectWithId(EMPLOYEE_1_ID, "_1", 8000.0, company);
    EmployeeUpdateInfo employeeUpdateInfo = convertToEmployeeUpdateDto(savedEmployee);
    employeeUpdateInfo.setName("John Doe");
    employeeUpdateInfo.setAddress("Brite Str, 38, 50670");
    when(employeeRepository.findById(EMPLOYEE_1_ID)).thenReturn(Optional.empty());
    //when
    var thrown = Assertions.assertThrows(DataNotFoundException.class, () -> {
      var employeeDtoResponse = employeeService.update(EMPLOYEE_1_ID, employeeUpdateInfo);
    });

    //then
    verify(employeeRepository).findById(EMPLOYEE_1_ID);
    assertThat(thrown.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(thrown.getMessage()).isEqualTo(EmployeeServiceErrorCode.EMPLOYEE_NOT_FOUND.getMessage());
  }

  @Test
  public void shouldRemoveEmployee() {
    //given

    //when
    employeeService.removeEmployee(EMPLOYEE_1_ID);
    //then
    verify(employeeRepository).deleteById(EMPLOYEE_1_ID);
  }

  @Test
  public void shouldRemoveEmployee_throwsDataNotFound() {
    //given
    doThrow(EmptyResultDataAccessException.class).when(employeeRepository).deleteById(EMPLOYEE_1_ID);
    //when
    var thrown = Assertions.assertThrows(DataNotFoundException.class, () -> {
      employeeService.removeEmployee(EMPLOYEE_1_ID);
    });
    //then
    verify(employeeRepository).deleteById(EMPLOYEE_1_ID);
    assertThat(thrown.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(thrown.getMessage()).contains(String.valueOf(EMPLOYEE_1_ID));
  }

  @Test
  public void shouldGetAverageSalaryByCompanyId() {
    //given
    when(employeeRepository.averageSalaryByCompanyId(COMPANY_1_ID)).thenReturn(12000.0);
    //when
    Double avgSalary = employeeService.findAverageSalary(COMPANY_1_ID);
    //then
    verify(employeeRepository).averageSalaryByCompanyId(COMPANY_1_ID);
    assertThat(avgSalary).isEqualTo(12000.0);
  }

  @Test
  public void shouldGetAverageSalaryByCompanyId_returnZero() {
    //given
    when(employeeRepository.averageSalaryByCompanyId(COMPANY_1_ID)).thenReturn(null);
    //when
    Double avgSalary = employeeService.findAverageSalary(COMPANY_1_ID);
    //then
    verify(employeeRepository).averageSalaryByCompanyId(COMPANY_1_ID);
    assertThat(avgSalary).isEqualTo(0.0);
  }


  private Employee createEmployeeObject(String nameSuffix, Double salary, Company company) {
    String employeeName = NAME_PREFIX + nameSuffix;
    Employee employee = Employee.builder().name(employeeName).address(DEFAULT_ADDRESS)
                            .email(employeeName + EMAIL_SUFFIX).salary(salary).surname(employeeName).build();
    employee.setCompany(company);
    return employee;
  }

  private Company createCompanyObject(long companyId, String companyName) {
    return Company.builder().id(companyId).name(companyName).build();
  }

  private Employee createEmployeeObjectWithId(long id, String nameSuffix, Double salary, Company company) {
    Employee employee = createEmployeeObject(nameSuffix, salary, company);
    employee.setId(id);
    return employee;
  }

  private Employee getEmployeeCloneWithId(long id, Employee employee) {
    Employee savedEmployee = new Employee();
    BeanUtils.copyProperties(employee, savedEmployee);
    savedEmployee.setId(EMPLOYEE_1_ID);
    return savedEmployee;
  }

  private EmployeeUpdateInfo convertToEmployeeUpdateDto(Employee employee) {
    EmployeeUpdateInfo employeeUpdateInfo = new EmployeeUpdateInfo();
    employeeUpdateInfo.setName(employee.getName());
    employeeUpdateInfo.setSurname(employee.getSurname());
    employeeUpdateInfo.setEmail(employee.getEmail());
    employeeUpdateInfo.setAddress(employee.getAddress());
    employeeUpdateInfo.setSalary(employee.getSalary());
    return employeeUpdateInfo;
  }

  private EmployeeInsertInfo convertToEmployeeInsertDto(Employee employee) {
    EmployeeInsertInfo employeeInsertDto = new EmployeeInsertInfo();
    employeeInsertDto.setName(employee.getName());
    employeeInsertDto.setSurname(employee.getSurname());
    employeeInsertDto.setEmail(employee.getEmail());
    employeeInsertDto.setAddress(employee.getAddress());
    employeeInsertDto.setSalary(employee.getSalary());
    employeeInsertDto.setCompanyId(employee.getCompany().getId());
    return employeeInsertDto;
  }

}
