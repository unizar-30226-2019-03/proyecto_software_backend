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
 * Excepcion para gestionar que las estampillas temporales de los
 * comentarios esten dentro de la duracion del video
 */

@SuppressWarnings("serial")
public class InvalidTokenException extends Error {
   public InvalidTokenException(String errorMessage) {
       super(errorMessage);
   }
}