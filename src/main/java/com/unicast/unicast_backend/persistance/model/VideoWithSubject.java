/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

package com.unicast.unicast_backend.persistance.model;

import java.net.URI;
import java.sql.Timestamp;

import org.springframework.data.rest.core.config.Projection;

/*
 * Relacion entre los videos y los usuarios que son sus propietarios
 */

@Projection(name = "videoWithSubject", types = { Video.class })
interface VideoWithSubject {

    // Coleccion de metodos getters para obtener los datos necesarios
    
    Long getId();

    String getTitle();

    String getDescription();

    Timestamp getTimestamp();

    URI getUrl();

    URI getThumbnailUrl();

    Integer getSeconds();

    Subject getSubject();

    Float getScore();
}