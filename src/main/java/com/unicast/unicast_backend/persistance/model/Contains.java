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
 * Relacion para determinar los videos que pertenecen a una lista de reproduccion de videos
 */

@Data
@Entity
@Table(name = "video_list")
public class Contains {
    
    // Clave de contencion de un video en una lista
	@EmbeddedId
    private ContainsKey id;
    
    // Relacion N:1 con video
	@ManyToOne
    @MapsId("fk_video")
    @JoinColumn(name = "fk_video")
    // Video contenido
    private Video video;
    
    // Relacion N:1 con lista de reproduccion
	@ManyToOne
    @MapsId("fk_list")
    @JoinColumn(name = "fk_list")
    // Lista de reproduccion
    private ReproductionList list;
    
    // Posicion que ocupa el video dentro de la lista
	private Integer position;
}
