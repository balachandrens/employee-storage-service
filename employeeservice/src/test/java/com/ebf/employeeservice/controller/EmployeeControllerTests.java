package com.ebf.employeeservice.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ebf.employeeservice.dto.EmployeeInsertDto;
import com.ebf.employeeservice.dto.EmployeeInsertInfo;
import com.ebf.employeeservice.dto.EmployeeResponseDto;
import com.ebf.employeeservice.dto.EmployeeUpdateDto;
import com.ebf.employeeservice.dto.EmployeeUpdateInfo;
import com.ebf.employeeservice.exception.DataNotFoundException;
import com.ebf.employeeservice.exception.EmployeeServiceErrorCode;
import com.ebf.employeeservice.restapi.controller.ControllerAdvice;
import com.ebf.employeeservice.restapi.controller.EmployeeController;
import com.ebf.employeeservice.service.EmployeeServiceDefaultImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTests {

  private MockMvc mvc;

  @Mock
  private EmployeeServiceDefaultImpl employeeService;

  @InjectMocks
  private EmployeeController employeeController;

  private JacksonTester<EmployeeInsertDto> jsonEmployeeInsertDto;
  private JacksonTester<EmployeeUpdateDto> jsonEmployeeUpdateDto;

  private static final String EMPLOYEES_BASE_URL = "/api/v1/employees";
  private static final String NAME_PREFIX = "Employee_";
  private static final String DEFAULT_ADDRESS = "Sample address";
  private static final String EMAIL_SUFFIX = "@mailinator.com";
  private static final long EMPLOYEE_1_ID = 1L;
  private static final long COMPANY_1_ID = 1L;
  private static final long EMPLOYEE_2_ID = 2L;
  private static final Double SALARY_1 = 12000.0;
  private static final Double SALARY_2 = 14000.0;
  private static final String INVALID_EMAIL = "mailinator.com";


  @BeforeEach
  public void setup() {
    JacksonTester.initFields(this, new ObjectMapper());
    mvc = MockMvcBuilders.standaloneSetup(employeeController).setControllerAdvice(new ControllerAdvice()).build();
  }


  @Test
  public void shouldAddEmployees() throws Exception {
    EmployeeInsertInfo employeeInsertInfo = createEmployeeInsertDto("_1", SALARY_1, COMPANY_1_ID);
    EmployeeResponseDto employeeResponseDto = convertToEmployeeResponseDto(EMPLOYEE_1_ID, employeeInsertInfo);
    doReturn(employeeResponseDto).when(employeeService).save(any(EmployeeInsertInfo.class));

    mvc.perform(MockMvcRequestBuilders.post(EMPLOYEES_BASE_URL).accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonEmployeeInsertDto.write(new EmployeeInsertDto(employeeInsertInfo)).getJson()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.employee.id").value(EMPLOYEE_1_ID))
        .andExpect(jsonPath("$.employee.name").value(employeeInsertInfo.getName()))
        .andExpect(jsonPath("$.employee.salary").value(SALARY_1));

  }

  @Test
  public void shouldAddEmployees_throwsDataValidationExceptionForEmail() throws Exception {
    EmployeeInsertInfo employeeInsertInfo = createEmployeeInsertDto("_1", SALARY_1, COMPANY_1_ID);
    employeeInsertInfo.setEmail(INVALID_EMAIL);
    mvc.perform(MockMvcRequestBuilders.post(EMPLOYEES_BASE_URL).accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonEmployeeInsertDto.write(new EmployeeInsertDto(employeeInsertInfo)).getJson()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(EmployeeServiceErrorCode.DATA_BIND_EXCEPTION.getErrorCode()))
        .andExpect(jsonPath("$.message", containsString("Email")));

  }

  @Test
  public void shouldAddEmployees_throwsDataValidationExceptionForName() throws Exception {
    EmployeeInsertInfo employeeInsertInfo = createEmployeeInsertDto("_1", SALARY_1, COMPANY_1_ID);
    employeeInsertInfo.setName("");
    mvc.perform(MockMvcRequestBuilders.post(EMPLOYEES_BASE_URL).accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonEmployeeInsertDto.write(new EmployeeInsertDto(employeeInsertInfo)).getJson()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(EmployeeServiceErrorCode.DATA_BIND_EXCEPTION.getErrorCode()))
        .andExpect(jsonPath("$.message", containsString("Name")));

  }

  @Test
  public void shouldGetAllEmployeesForCompany() throws Exception {
    EmployeeInsertInfo employeeInsertInfo1 = createEmployeeInsertDto("_1", SALARY_1, COMPANY_1_ID);
    EmployeeResponseDto employeeResponseDto1 = convertToEmployeeResponseDto(EMPLOYEE_1_ID, employeeInsertInfo1);
    EmployeeInsertInfo employeeInsertInfo2 = createEmployeeInsertDto("_2", SALARY_2, COMPANY_1_ID);
    EmployeeResponseDto employeeResponseDto2 = convertToEmployeeResponseDto(EMPLOYEE_2_ID, employeeInsertInfo2);
    doReturn(Arrays.asList(employeeResponseDto1, employeeResponseDto2)).when(employeeService)
        .findByCompanyId(COMPANY_1_ID);
    String queryUrl = String.format(EMPLOYEES_BASE_URL + "?companyId=%s", COMPANY_1_ID);
    mvc.perform(MockMvcRequestBuilders.get(queryUrl).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$.employees").isArray()).andExpect(jsonPath("$.employees.[0].id").value(EMPLOYEE_1_ID))
        .andExpect(jsonPath("$.employees.[1].id").value(EMPLOYEE_2_ID));
  }

  @Test
  public void shouldGetEmptyEmployeesListForCompany() throws Exception {
    doReturn(Collections.emptyList()).when(employeeService).findByCompanyId(COMPANY_1_ID);
    String queryUrl = String.format(EMPLOYEES_BASE_URL + "?companyId=%s", COMPANY_1_ID);
    mvc.perform(MockMvcRequestBuilders.get(queryUrl).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$.employees").isArray()).andExpect(jsonPath("$.employees.[0].id").doesNotExist());
  }

  @Test
  public void getEmployees_throwsExceptionWithoutQueryParam() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get(EMPLOYEES_BASE_URL).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(EmployeeServiceErrorCode.MISSING_PARAMETER_EXCEPTION.getErrorCode()))
        .andExpect(jsonPath("$.message", containsString("companyId")));
  }

  @Test
  public void shouldGetEmployeesById() throws Exception {
    EmployeeInsertInfo employeeInsertInfo = createEmployeeInsertDto("_1", SALARY_1, COMPANY_1_ID);
    EmployeeResponseDto employeeResponseDto = convertToEmployeeResponseDto(EMPLOYEE_1_ID, employeeInsertInfo);
    doReturn(employeeResponseDto).when(employeeService).findByEmployeeId(EMPLOYEE_1_ID);
    String queryUrl = String.format(EMPLOYEES_BASE_URL + "/%s", EMPLOYEE_1_ID);
    mvc.perform(MockMvcRequestBuilders.get(queryUrl).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$.employee").exists()).andExpect(jsonPath("$.employee.id").value(EMPLOYEE_1_ID))
        .andExpect(jsonPath("$.employee.name").value(employeeResponseDto.getName()));
  }

  @Test
  public void shouldGetEmployeesById_throwEmployeeNotFound() throws Exception {
    doThrow(new DataNotFoundException(EmployeeServiceErrorCode.EMPLOYEE_NOT_FOUND)).when(employeeService)
        .findByEmployeeId(EMPLOYEE_1_ID);
    String queryUrl = String.format(EMPLOYEES_BASE_URL + "/%s", EMPLOYEE_1_ID);
    mvc.perform(MockMvcRequestBuilders.get(queryUrl).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(EmployeeServiceErrorCode.EMPLOYEE_NOT_FOUND.getErrorCode()));
  }

  @Test
  public void shouldUpdateEmployee() throws Exception {
    EmployeeInsertInfo employeeInsertInfo = createEmployeeInsertDto("_1", SALARY_1, COMPANY_1_ID);
    EmployeeResponseDto employeeResponseDto = convertToEmployeeResponseDto(EMPLOYEE_1_ID, employeeInsertInfo);
    employeeResponseDto.setSalary(SALARY_2);
    EmployeeUpdateInfo employeeUpdateInfo = convertToEmployeeUpdateDto(employeeResponseDto);
    doReturn(employeeResponseDto).when(employeeService)
        .update(eq(EMPLOYEE_1_ID), any(EmployeeUpdateInfo.class));
    String queryUrl = String.format(EMPLOYEES_BASE_URL + "/%s", EMPLOYEE_1_ID);
    mvc.perform(
        MockMvcRequestBuilders.put(queryUrl).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .content(jsonEmployeeUpdateDto.write(new EmployeeUpdateDto(employeeUpdateInfo)).getJson()))
        .andExpect(status().isOk()).andExpect(jsonPath("$.employee.id").value(EMPLOYEE_1_ID))
        .andExpect(jsonPath("$.employee.name").value(employeeInsertInfo.getName()))
        .andExpect(jsonPath("$.employee.salary").value(SALARY_2));
  }

  @Test
  public void updateEmployee_throwsDataBindException() throws Exception {
    EmployeeInsertInfo employeeInsertInfo = createEmployeeInsertDto("_1", SALARY_1, COMPANY_1_ID);
    EmployeeResponseDto employeeResponseDto = convertToEmployeeResponseDto(EMPLOYEE_1_ID, employeeInsertInfo);
    employeeResponseDto.setEmail(INVALID_EMAIL);
    EmployeeUpdateInfo employeeUpdateInfo = convertToEmployeeUpdateDto(employeeResponseDto);
    String queryUrl = String.format(EMPLOYEES_BASE_URL + "/%s", EMPLOYEE_1_ID);
    mvc.perform(
        MockMvcRequestBuilders.put(queryUrl).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .content(jsonEmployeeUpdateDto.write(new EmployeeUpdateDto(employeeUpdateInfo)).getJson()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(EmployeeServiceErrorCode.DATA_BIND_EXCEPTION.getErrorCode()))
        .andExpect(jsonPath("$.message", containsString("Email")));
  }

  @Test
  public void updateEmployee_throwsEmployeeNotFound() throws Exception {
    EmployeeInsertInfo employeeInsertInfo = createEmployeeInsertDto("_1", SALARY_1, COMPANY_1_ID);
    EmployeeResponseDto employeeResponseDto = convertToEmployeeResponseDto(EMPLOYEE_1_ID, employeeInsertInfo);
    employeeResponseDto.setSalary(SALARY_2);
    EmployeeUpdateInfo employeeUpdateInfo = convertToEmployeeUpdateDto(employeeResponseDto);
    doThrow(new DataNotFoundException(EmployeeServiceErrorCode.EMPLOYEE_NOT_FOUND)).when(employeeService)
        .update(eq(EMPLOYEE_1_ID), any(EmployeeUpdateInfo.class));
    String queryUrl = String.format(EMPLOYEES_BASE_URL + "/%s", EMPLOYEE_1_ID);
    mvc.perform(
        MockMvcRequestBuilders.put(queryUrl).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .content(jsonEmployeeUpdateDto.write(new EmployeeUpdateDto(employeeUpdateInfo)).getJson()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(EmployeeServiceErrorCode.EMPLOYEE_NOT_FOUND.getErrorCode()));
  }

  @Test
  public void shouldDeleteEmployeeById() throws Exception {
    String queryUrl = EMPLOYEES_BASE_URL + "/" + EMPLOYEE_1_ID;
    doNothing().when(employeeService).removeEmployee(EMPLOYEE_1_ID);
    mvc.perform(MockMvcRequestBuilders.delete(queryUrl)).andExpect(status().isNoContent());
  }

  @Test
  public void deleteEmployeeById_throwsEmployeeNotFoundException() throws Exception {
    String queryUrl = EMPLOYEES_BASE_URL + "/" + EMPLOYEE_1_ID;
    doThrow(new DataNotFoundException(EmployeeServiceErrorCode.EMPLOYEE_NOT_FOUND)).when(employeeService).removeEmployee(EMPLOYEE_1_ID);
    mvc.perform(MockMvcRequestBuilders.delete(queryUrl))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(EmployeeServiceErrorCode.EMPLOYEE_NOT_FOUND.getErrorCode()));
  }

  private EmployeeResponseDto convertToEmployeeResponseDto(long employeeId, EmployeeInsertInfo employeeInsertInfo) {
    EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto();
    BeanUtils.copyProperties(employeeInsertInfo, employeeResponseDto);
    employeeResponseDto.setId(employeeId);
    return employeeResponseDto;
  }

  private EmployeeInsertInfo createEmployeeInsertDto(String nameSuffix, Double salary, Long companyId) {
    String employeeName = NAME_PREFIX + nameSuffix;
    return EmployeeInsertInfo.builder().name(employeeName).surname(employeeName).address(DEFAULT_ADDRESS)
               .email(employeeName + EMAIL_SUFFIX).salary(salary).companyId(companyId).build();
  }

  private EmployeeUpdateInfo convertToEmployeeUpdateDto(EmployeeResponseDto employee) {
    EmployeeUpdateInfo employeeUpdateInfo = new EmployeeUpdateInfo();
    employeeUpdateInfo.setName(employee.getName());
    employeeUpdateInfo.setSurname(employee.getSurname());
    employeeUpdateInfo.setEmail(employee.getEmail());
    employeeUpdateInfo.setAddress(employee.getAddress());
    employeeUpdateInfo.setSalary(employee.getSalary());
    return employeeUpdateInfo;
  }

}
