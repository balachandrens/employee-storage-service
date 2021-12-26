package com.ebf.employeeservice.controller;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ebf.employeeservice.exception.EmployeeServiceErrorCode;
import com.ebf.employeeservice.restapi.controller.CompanyController;
import com.ebf.employeeservice.restapi.controller.ControllerAdvice;
import com.ebf.employeeservice.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerTests {

  private MockMvc mockMvc;

  @Mock
  EmployeeService employeeService;

  @InjectMocks
  CompanyController companyController;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(companyController).setControllerAdvice(new ControllerAdvice()).build();
  }

  private static final String COMPANIES_BASE_URL = "/api/v1/companies";
  private static final long COMPANY_1_ID = 1L;
  private static final double AVERAGE_SALARY_1 = 12000.0;

  @Test
  public void shouldGetAverageEmployeeSalary() throws Exception {
    String queryUrl = COMPANIES_BASE_URL + "/" + COMPANY_1_ID + "/average-salary";
    doReturn(AVERAGE_SALARY_1).when(employeeService).findAverageSalary(COMPANY_1_ID);
    mockMvc.perform(MockMvcRequestBuilders.get(queryUrl)).andExpect(status().isOk())
        .andExpect(jsonPath("$.averageSalary").value(AVERAGE_SALARY_1));
  }

  @Test
  public void getAverageEmployeeSalary_throwsException() throws Exception {
    String queryUrl = COMPANIES_BASE_URL + "/test/average-salary";
    mockMvc.perform(MockMvcRequestBuilders.get(queryUrl)).andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(EmployeeServiceErrorCode.ARGUMENT_MISMATCH_EXCEPTION.getErrorCode()));
  }

}
