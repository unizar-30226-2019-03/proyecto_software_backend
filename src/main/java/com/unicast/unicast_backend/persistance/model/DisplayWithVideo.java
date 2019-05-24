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

import java.sql.Timestamp;

import org.springframework.data.rest.core.config.Projection;

/*
 * Relacion entre los videos y los displays que se han realizado en cada uno
 */

@Projection(name = "displayWithVideo", types = { Display.class })
interface DisplayWithVideo {

    // Coleccion de metodos getters para poder extraer los datos necesarios 

    DisplayKey getId();

    Video getVideo();

    Timestamp getTimestampLastTime();

    Integer getSecsFromBeg();
}