package com.ebf.employeeservice.util;

import com.ebf.employeeservice.dto.Response;

public interface ResponseBuilder {

  default  <Data> Response<Data> buildResponse(Data data) {
    return new Response<>(data);
  }

  default  <Data> Response<Data> buildResponse(String message, Data data) {
    return new Response<>(message, data);
  }

  default  <Data> Response<Data> buildResponse(String message, Data data, String code) {
    return new Response<>(message, code, data);
  }

  default Response buildResponse(String message) {
    return buildResponse(message, null);
  }
}