package com.ebf.employeeservice.dto;

import com.ebf.employeeservice.exception.EmployeeServiceErrorCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime timestamp = LocalDateTime.now();
  private String message;
  private String code;

  public ErrorResponse() {
    this.message = "Unexpected error";
  }

  public ErrorResponse(String message) {
    this.message = message;
  }

  public ErrorResponse(String message, String code) {
    this.message = message;
    this.code = code;
  }

  public ErrorResponse(EmployeeServiceErrorCode errorCode) {
    this.message = errorCode.getMessage();
    this.code = errorCode.getErrorCode();
  }

}
