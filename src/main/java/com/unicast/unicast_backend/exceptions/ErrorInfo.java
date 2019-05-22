package com.unicast.unicast_backend.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.http.HttpException;

import lombok.Data;

@Data
public class ErrorInfo {

   @JsonProperty("message")
   private String message;
   @JsonProperty("status_code")
   private int statusCode;
   @JsonProperty("uri")
   private String uriRequested;

   public ErrorInfo(HttpException exception, String uriRequested) {
       this.message = exception.getMessage();
       //this.statusCode = exception.getStatusCode().value();
       this.uriRequested = uriRequested;
   }

   public ErrorInfo(int statusCode, String message, String uriRequested) {
       this.message = message;
       this.statusCode = statusCode;
       this.uriRequested = uriRequested;
   }
}
