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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/*
 * Relacion N:M entre los videos y los votos que reciben
 */

@Data
@Embeddable
public class VoteKey implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Identificador del video que recibe voto
	@Column(name = "fk_video")
    private Long videoId;
    
    // Identificador del voto dado al video
	@Column(name = "fk_app_user")
    private Long userId;
	
}