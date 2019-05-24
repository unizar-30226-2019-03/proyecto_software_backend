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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.amazonaws.AmazonServiceException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


/*
 * Control para la gestion de archivos en el servicio de 
 * almacenamiento libre de amazon s3
 */

@Component
public abstract class S3FileHandler {
    @Autowired

    // Datos de configuracion del servicio
    private S3Constants s3Constants;

    // Longitud maxima de los nombres de los archivos a gestionar
    private static Integer FILE_KEY_LENGTH = 10;

    // Ultimo fichero con el que se ha trabajado
    private File lastFile;

    // Extension del fichero
    abstract protected String getFilePrefix();

    // Carpeta o directorio donde se encuentra el fichero
    abstract protected String getFileFolder();

    /*
     * Permite subir un fichero al servicio s3 de amazon
     * Parametros
     * @param uploadedFile: nombre del fichero a subir
     */
    public URI uploadFile(MultipartFile uploadedFile)
            throws IllegalStateException, IOException, AmazonServiceException, URISyntaxException {

        // Creacion del fochero que se desea subir
        File file = File.createTempFile(getFilePrefix(), uploadedFile.getOriginalFilename());
        uploadedFile.transferTo(file);

        // Establecimiento de la ruta del fichero a subir
        final String s3Key = getFileFolder() + "/" + getFilePrefix()
                + RandomStringUtils.randomAlphanumeric(FILE_KEY_LENGTH);
        s3Constants.s3.putObject(s3Constants.BUCKET_NAME, s3Key, file);

        // Guardarlo como ultimo fichero subido
        lastFile = file;

        // Devolver respuesta de la operacion
        return new URI(s3Constants.s3.getResourceUrl(s3Constants.BUCKET_NAME, s3Key));
    }

    /*
     * Permite borrar un fichero del servicio de amazon 
     * Parametros
     * @param URI: identificador de recurso uniforme correspondiente al fichero a borrar
     */
    public void deleteFile(URI file) {

        // Obtencion de la ruta o path del fichero
        String fileName = file.getPath();

        // Eliminarlo del servicio s3 de amazon
        s3Constants.s3.deleteObject(s3Constants.BUCKET_NAME, fileName.substring(1, fileName.length()));
    }
 
    /*
     * Permite obtener el ultimo fichero que se ha subido 
     */
    public File getLastUploadedTmpFile() {
        return lastFile;
    }

    /*
     * Permite borrar de forma directa el ultimo fichero que se ha subido
     */
    public void deleteLastUploadedTmpFile() {
        lastFile.delete();
    }
}
