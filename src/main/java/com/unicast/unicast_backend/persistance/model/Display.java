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

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Data;


/*
 * Objeto display correspondiente al objeto display en la base de
 * datos
 */

@Data
@Entity
@Table(name = "app_user_video_watches")
public class Display {
    
    // Identificador del video
	@EmbeddedId
    private DisplayKey id;
    
    // Relacion N:1 con video
	@ManyToOne
    @MapsId("fk_video")
    @JoinColumn(name = "fk_video")
    // Video donde se va a anyadir el display
    private Video video;
    
    // Relacion N:1 con usuario
	@ManyToOne
    @MapsId("fk_app_user")
    @JoinColumn(name = "fk_app_user")
    // Usuario que efectua el display
    private User user;
    
    // Estampilla temporal del display
    private Timestamp timestampLastTime;
    
    // Segundos en los que se hace el display con respecto al principio del video
    private Integer secsFromBeg;
	
	
}