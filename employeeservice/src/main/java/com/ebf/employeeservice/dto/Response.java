package com.ebf.employeeservice.dto;

import com.ebf.employeeservice.exception.EmployeeServiceErrorCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Response<Data> {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime timestamp = LocalDateTime.now();
  private String message;
  private String code;
  private Data data;

  public Response() {
    this.message = "Unexpected error";
  }

  public Response(Data data) {
    this.data = data;
  }

  public Response(String message) {
    this.message = message;
  }

  public Response(String message, Data data) {
    this.message = message;
    this.data = data;
  }

  public Response(String message, String code) {
    this.message = message;
    this.code = code;
  }

  public Response(String message, String code, Data data) {
    this.message = message;
    this.code = code;
    this.data = data;
  }

  public Response(EmployeeServiceErrorCode errorCode) {
    this.message = errorCode.getMessage();
    this.code = errorCode.getErrorCode();
  }

}
