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
  * Excepcion para controlar que los videos subidos tiene formato exclusivo
  * mp4
  */

@SuppressWarnings("serial")
public class NotMp4Exception extends Exception { 
    public NotMp4Exception(String errorMessage) {
        super(errorMessage);
    }
}
