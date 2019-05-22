/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

 /*
  * Excepcion para controlar que un usuario con permisos de administracion
  * no pueda mandar mensajes a otros usuarios
  */
  
 package com.unicast.unicast_backend.exceptions;

@SuppressWarnings("serial")
public class NotAdminSenderException extends Exception {
    public NotAdminSenderException(String errorMessage) {
        super(errorMessage);
    }
}
