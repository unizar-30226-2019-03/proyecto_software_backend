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
  * Excepcion para controlar que el destinatario de un correo solo pueda
  * ser un usuario profesor
  */
@SuppressWarnings("serial")
public class NotProfessorReceiver extends Exception {
    public NotProfessorReceiver(String errorMessage) {
        super(errorMessage);
    }
}
