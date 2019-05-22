/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

package com.unicast.unicast_backend.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.http.HttpException;

import lombok.Data;


/*
 * Clase que representa la estructura del archivo json
 * donde se han especificado las propiedades y el formato a
 * devolver en caso de excepcion
 */

@Data
public class ErrorInfo {

   @JsonProperty("message")
   private String message;
   @JsonProperty("status_code")
   private int statusCode;
   @JsonProperty("uri")
   private String uriRequested;

   /* Constructores de la clase */

   public ErrorInfo(HttpException exception, String uriRequested) {
       this.message = exception.getMessage();
       this.uriRequested = uriRequested;
   }

   public ErrorInfo(int statusCode, String message, String uriRequested) {
       this.message = message;
       this.statusCode = statusCode;
       this.uriRequested = uriRequested;
   }
}
