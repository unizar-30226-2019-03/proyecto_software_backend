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
 * Excepcion para controlar que el fichero que se desea subir es si o si 
 * una imagen
 */

@SuppressWarnings("serial")
public class NotImageException extends Error { 
   public NotImageException(String errorMessage) {
       super(errorMessage);
   }
}
