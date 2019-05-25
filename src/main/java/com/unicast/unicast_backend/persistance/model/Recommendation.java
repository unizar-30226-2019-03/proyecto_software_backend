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

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Data;

/*
 * Entidad para poder modelar el sistema de recomendaciones de videos
 * a los usuarios
 */

@Data
@Entity
@Table(name = "app_user_recommendation")
public class Recommendation {
    
    // Identificador de la recomendacion
	@EmbeddedId
    private RecommendationKey id;
    
    // Relacion N:1 con video
	@ManyToOne
    @MapsId("fk_video")
    @JoinColumn(name = "fk_video")
    // Video a recomendar
    private Video video;
    
    // Relacion N:1 con usuario
	@ManyToOne
    @MapsId("fk_app_user")
    @JoinColumn(name = "fk_app_user")
    // Usuario al que se le recomienda el video
    private User user;
    
    // Posicion del video en la recomendacion
    private Integer position;
}
