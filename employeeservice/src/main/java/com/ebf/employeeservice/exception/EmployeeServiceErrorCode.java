package com.ebf.employeeservice.exception;

import lombok.Getter;

@Getter
public enum EmployeeServiceErrorCode {

  DATA_NOT_FOUND("EBF_ES_001", "Data not found"),
  EMPLOYEE_NOT_FOUND("EBF_ES_002", "Employee not found"),
  EMPLOYEE_NOT_FOUND_FOR_COMPANY("EBF_ES_003", "Employee not found for company"),
  COMPANY_NOT_FOUND("EBF_ES_004", "Company not found"),
  DATA_BIND_EXCEPTION("EBF_ES_005", "Data bind exception");



  private final String errorCode;
  private final String message;

  EmployeeServiceErrorCode(String errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }

}
