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
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Formula;

import lombok.Data;

/*
 * Entidad de listas de reproduccion de videos
 */

@Data
@Entity
@Table(name = "reproduction_list")
public class ReproductionList {
    
    // Identificador de la lista de reproduccion
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de la lista de reproduccion
    private String name;
    
    // Relacion N:1 con usuario
    @ManyToOne
    @JoinColumn(name = "fk_app_user")
    // Propietario de la lista
    private User user;

    // Relacion 1:N con video
    @JsonIgnore
    @OneToMany(mappedBy = "list")
    // Listado de videos recogidos en la lista de reproduccion
    private Collection<Contains> videoList;

    // Consulta SQL para devolver el numero de videos de una lista
    @Formula("(select count(*) from video_list vl where vl.fk_list = id)")
    private Integer numVideos;
    
    // Consulta SQL para obtener la miniatura de una lista de reproduccion
    @Formula("(select v.thumbnail_url from video_list vl join video v on vl.fk_video = v.id where vl.fk_list = id and vl.position = 1)")
    private URI thumbnail;
}
