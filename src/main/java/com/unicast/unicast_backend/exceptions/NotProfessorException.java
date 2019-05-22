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
 * Excepcion que gestiona que solo los usuarios con permisos de profesor
 * puedan dar permisos de profesor a otros usuarios
 */

@SuppressWarnings("serial")
public class NotProfessorException extends Exception {
    public NotProfessorException(String errorMessage) {
        super(errorMessage);
    }
}
