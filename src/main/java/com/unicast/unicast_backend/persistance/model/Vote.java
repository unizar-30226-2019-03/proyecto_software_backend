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
 * Entidad de votos
 */

@Data
@Entity
@Table(name = "app_user_video_vote")
public class Vote {
    
    // Identificador del voto
	@EmbeddedId
    private VoteKey id;
    
    // Relacion N:1 con video
	@ManyToOne
    @MapsId("fk_video")
    @JoinColumn(name = "fk_video")
    // Video votado
    private Video video;
    
    // Relacion N:1 con usuario
	@ManyToOne
    @MapsId("fk_app_user")
    @JoinColumn(name = "fk_app_user")
    // Usuario que vota
    private User user;
    
    // Claridad del video
    private Integer clarity;
    
    // Calidad del video
    private Integer quality;
    
    // Amenidad del video
    private Integer suitability;
}
