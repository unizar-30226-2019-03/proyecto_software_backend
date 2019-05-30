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
 * Excepcion para controlar que un profesor pueda subir videos de
 * asignaturas a las que esta suscrito
 */

@SuppressWarnings("serial")
public class ProfessorNotInUniException extends Exception {
   public ProfessorNotInUniException(String errorMessage) {
       super(errorMessage);
   }
}
