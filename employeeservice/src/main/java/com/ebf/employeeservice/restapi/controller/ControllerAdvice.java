package com.ebf.employeeservice.restapi.controller;

import com.ebf.employeeservice.dto.Response;
import com.ebf.employeeservice.exception.DataNotFoundException;
import com.ebf.employeeservice.exception.EmployeeServiceErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@org.springframework.web.bind.annotation.ControllerAdvice()
public class ControllerAdvice {

  private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAdvice.class);

  @ExceptionHandler(DataNotFoundException.class)
  public ResponseEntity<Response> handleDataNotFoundException(DataNotFoundException ex) {
    LOGGER.debug("Error Occurred {}:{}", ex.getMessage(), ex);
    Response errorResponse = new Response(ex.getMessage(), ex.getEmployeeServiceErrorCode().getErrorCode());
    return new ResponseEntity<>(errorResponse, new HttpHeaders(), ex.getHttpStatus());
  }

  @ExceptionHandler({BindException.class, HttpMessageNotReadableException.class})
  public ResponseEntity<Response> handleBindException(BindException ex) {
    LOGGER.debug("Error Occurred {}:{}", ex.getBindingResult().getFieldError(), ex);
    Response errorResponse = new Response(ex.getBindingResult().getFieldError().getDefaultMessage(),
        EmployeeServiceErrorCode.DATA_BIND_EXCEPTION.getErrorCode());
    return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({MissingServletRequestParameterException.class})
  public ResponseEntity<Response> handleMissingServletRequestParameterException(
      MissingServletRequestParameterException ex) {
    LOGGER.debug("Error Occurred {}:{}", ex.getMessage(), ex);
    Response errorResponse = new Response(ex.getMessage(),
        EmployeeServiceErrorCode.MISSING_PARAMETER_EXCEPTION.getErrorCode());
    return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Response> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    LOGGER.debug("Error Occurred {}:{}", ex.getMessage(), ex);
    Response errorResponse = new Response(ex.getMessage(),
        EmployeeServiceErrorCode.ARGUMENT_MISMATCH_EXCEPTION.getErrorCode());
    return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }



}
