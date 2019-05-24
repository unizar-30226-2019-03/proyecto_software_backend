/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

package com.unicast.unicast_backend.s3handlers;

import org.springframework.stereotype.Component;

/*
 * Rutina para el control y gestion de subida de videos 
 */

@Component
public final class S3VideoHandler extends S3FileHandler {

    /*
     * Permite obtener el prefijo de los videos
     */
    @Override
    protected String getFilePrefix() {
        return "vid";
    }


    /*
     * Permite obtener el directorio de almacenamiento de los videos
     */
    @Override
    protected String getFileFolder() {
        return "videos";
    }
}
