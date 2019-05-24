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
 * Relacion entre los usuarios y los videos que se le recomiendan
 */

@Data
@Embeddable
public class RecommendationKey implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Identificador del video recomendado
	@Column(name = "fk_video")
    private Long videoId;
    
    // Identificador del usuario al que se le recomienda
	@Column(name = "fk_app_user")
    private Long userId;
	
}