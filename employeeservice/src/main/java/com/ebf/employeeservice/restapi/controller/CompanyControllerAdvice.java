package com.ebf.employeeservice.restapi.controller;

import com.ebf.employeeservice.dto.ErrorResponse;
import com.ebf.employeeservice.exception.DataNotFoundException;
import com.ebf.employeeservice.exception.EmployeeServiceErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice()
public class CompanyControllerAdvice {

  private static final Logger LOGGER = LoggerFactory.getLogger(CompanyControllerAdvice.class);

  @ExceptionHandler(DataNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException ex) {
    LOGGER.debug("Error Occurred {}:{}", ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(ex.getEmployeeServiceErrorCode());
    return new ResponseEntity<>(errorResponse, new HttpHeaders(), ex.getHttpStatus());
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
    LOGGER.debug("Error Occurred {}:{}", ex.getBindingResult().getFieldError(), ex);
    ErrorResponse errorResponse = new ErrorResponse(ex.getBindingResult().getFieldError().getDefaultMessage(),
        EmployeeServiceErrorCode.DATA_BIND_EXCEPTION.getErrorCode());
    return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

}
