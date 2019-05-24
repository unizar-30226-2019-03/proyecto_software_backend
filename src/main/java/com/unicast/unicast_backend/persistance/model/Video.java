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
import java.sql.Timestamp;
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
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;
import org.springframework.data.rest.core.annotation.RestResource;

import lombok.Data;

/*
 * Entidad video
 */

@Data
@Entity
@Table(name = "video")
public class Video {

    // Identificador del video
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Titulo del video
    private String title;

    // Descrpcion del contenido del video
    private String description;

    // Estanpilla de subida
    private Timestamp timestamp;

    // Recurso uniforme para localizar el video una vez colgado
    private URI url;

    // Miniatura del video
    private URI thumbnailUrl;

    // Duraciob en segundos
    private Integer seconds;

    // Relacion N:1 con universidad
    @ManyToOne
        @JoinColumnsOrFormulas({
            @JoinColumnOrFormula(formula=@JoinFormula(value="(select u.id from subject s join university u on s.fk_university = u.id where fk_subject = s.id)", referencedColumnName = "id"))
        })
    // Universidad a la que pertenece el video
    private University university;

    // Relacion N:1 con asignatura
    @ManyToOne
    @JoinColumn(name = "fk_subject")
    // Asignatura a la que pertenece
    private Subject subject;

    // Relacion N:1 con el usuario
    @ManyToOne
    @JoinColumn(name = "fk_uploader")
    // Usuario que lo sube
    private User uploader;

    // Relacion 1:N con comentarios
    @OneToMany(mappedBy = "video")
    // Coleccion de comentarios asociados al video
    private Collection<Comment> comments;

    // Relacion 1:N con voto
    @OneToMany(mappedBy = "video")
    // Coleccion de votos asociados al video
    private Collection<Vote> votes;

    // Relacion 1:N con display
    @JsonIgnore
    @OneToMany(mappedBy = "video")
    @RestResource(exported = false)
    // Coleccion de displays asociados al video
    private Collection<Display> displays;
    
    // Consulta para poder obtener la puntuacion de un video en base a su
    // calidad, claridad y amenidad
    @Formula("(select avg((v.quality + v.clarity + v.suitability) / 3.0) from app_user_video_vote v where id = v.fk_video)")
    private Float score;

}
