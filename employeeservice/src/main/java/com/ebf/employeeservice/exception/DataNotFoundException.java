package com.ebf.employeeservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class DataNotFoundException extends EmployeeServiceException {

  @Getter
  private final HttpStatus httpStatus;

  public DataNotFoundException() {
    super(EmployeeServiceErrorCode.DATA_NOT_FOUND);
    httpStatus = HttpStatus.NOT_FOUND;
  }

  public DataNotFoundException(String message) {
    super(EmployeeServiceErrorCode.DATA_NOT_FOUND, message);
    httpStatus = HttpStatus.NOT_FOUND;
  }

  public DataNotFoundException(EmployeeServiceErrorCode errorCode) {
    super(errorCode);
    httpStatus = HttpStatus.NOT_FOUND;
  }

  public DataNotFoundException(EmployeeServiceErrorCode errorCode, String message) {
    super(errorCode, message);
    httpStatus = HttpStatus.NOT_FOUND;
  }

}
