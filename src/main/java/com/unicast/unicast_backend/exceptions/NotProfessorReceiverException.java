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
 * Excepcion de control de eliminado de listas de reproduccion
 * marcadas como favoritos
 */
@SuppressWarnings("serial")
public class NotProfessorReceiverException extends Exception {
    public NotProfessorReceiverException(String errorMessage) {
        super(errorMessage);
    }
}
