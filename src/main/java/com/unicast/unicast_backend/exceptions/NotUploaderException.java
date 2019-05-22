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

 /*
  * Excepcion para controlar que un usuario solo pueda subir videos de los que
  * es propietario
  */
  
@SuppressWarnings("serial")
public class NotUploaderException extends Exception {
    public NotUploaderException(String errorMessage) {
        super(errorMessage);
    }
}
