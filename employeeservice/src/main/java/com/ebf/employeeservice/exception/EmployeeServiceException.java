package com.ebf.employeeservice.exception;

import lombok.Getter;

public class EmployeeServiceException extends RuntimeException{

  @Getter
  private EmployeeServiceErrorCode employeeServiceErrorCode;

  public EmployeeServiceException(EmployeeServiceErrorCode employeeServiceErrorCode){
    super(employeeServiceErrorCode.getMessage());
    this.employeeServiceErrorCode = employeeServiceErrorCode;
  }

  public EmployeeServiceException(EmployeeServiceErrorCode employeeServiceErrorCode, String message){
    super(message);
    this.employeeServiceErrorCode = employeeServiceErrorCode;
  }

}
