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

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/*
 * Panel de control del servicio de configuracion de s3 de amazon
 */

@Configuration
public class S3Constants {

    // Region del cliente donde se monta el servicio
    public final String CLIENT_REGION;

    // Nombre del bucket de amazon para el almacenamiento de archivos
    public final String BUCKET_NAME;

    // Tipo de cliente 
    public final AmazonS3Client s3;


    /*
     * Permite llevar a cabo la configuracion de los parametros basicos del
     * servicio s3 de amazon
     * Parametros
     * @param cleinteRegion: region donde se va a montar el servicio
     * @param bucketName: nombre del bucket de amazon para el almacenamiento de ficheros
     */
    public S3Constants(@Value("${unicast.s3.client_region}") String clientRegion,
            @Value("${unicast.s3.bucket_name}") String bucketName) {
        CLIENT_REGION = clientRegion;
        BUCKET_NAME = bucketName;
        s3 = (AmazonS3Client) AmazonS3ClientBuilder.standard().withRegion(clientRegion).build();
    }

}