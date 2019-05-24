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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.amazonaws.AmazonServiceException;
import com.unicast.unicast_backend.exceptions.NotImageException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/*
 * Rutina para el control y gestion de subida de imagenes 
 */

@Component
public final class S3ImageHandler extends S3FileHandler {

    /*
     * Permite obtener el prefijo de las imagenes
     */
    @Override
    protected String getFilePrefix() {
        return "img";
    }

    /*
     * Permite obtener el directorio de almacenamiento de las imagenes
     */
    @Override
    protected String getFileFolder() {
        return "images";
    }

    /*
     * Permite llevar a cabo la subida de una imagen
     * Parametros
     * @param uploadFile: fichero imagen que se desea subir
     */
    @Override
    public URI uploadFile(MultipartFile uploadedFile)
        throws IllegalStateException, IOException, AmazonServiceException, URISyntaxException {
        
        // Comprobacion de si el fichero que se desea subir es una imagen
        // Lanza excepcion si no lo es
        if (!MediaType.parseMediaType(uploadedFile.getContentType()).getType().equals("image")) {
            throw new NotImageException("El fichero subido no es una imagen");
        }

        // Subida del fichero imagen
        return super.uploadFile(uploadedFile);
    }

}
