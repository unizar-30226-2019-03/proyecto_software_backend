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

import org.springframework.data.rest.core.config.Projection;

/*
 * Relacion entre las asignaturas y los videos donde se imparten
 */

@Projection(name = "subjectWithUniversity", types = { Subject.class })
interface SubjectWithUniversity {

    // Coleccion de metodos getters para poder extraer los datos necesarios
    
    Long getId();

    String getName();

    String getAbbreviation();

    University getUniversity();

    Double getAvgScore();
}