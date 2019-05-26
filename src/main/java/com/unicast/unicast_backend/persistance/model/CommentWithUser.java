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
 * Relacion entre los comentarios y los usuarios que los realizan
 */

@Projection(name = "commentWithUser", types = { Comment.class })
interface CommentWithUser {

    // Interfaz con los metodos getters para poder obtener los datos necesarios
    
    Long getId();

    String getText();

    Timestamp getTimestamp();

    Integer getSecondsFromBeginning();
    
    User getUser();
}