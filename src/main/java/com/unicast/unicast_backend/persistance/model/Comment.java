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
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

/*
 * Objeto comentario correspondiente con la entidad
 * comentario en la base de datos
 */

@Data
@Entity
@Table(name = "comment")
public class Comment {
    
    // Identificador del comentario
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Contenido del comentario
    private String text;

    // Estampilla temporal del momento de realizacion del comentario
    private Timestamp timestamp;

    // Instante en segundos donde se realiza el comentario
    @Column(name="secs_from_beg")
    private Integer secondsFromBeginning;
    
    // Relacion N:1 con video
    // Clave foraneo de video
    @ManyToOne
    @JoinColumn(name = "fk_video")
    private Video video;
    
    // Relacion N:1 con video
    // Clave foraneo de video
    @ManyToOne
    @JoinColumn(name = "fk_app_user")
    private User user;

    // Relacion N:1 con comentario respuesta
    // Relacion reflexiva para adimitir respuestas a un comentario
    // Clave foraneo de comentario
    @ManyToOne
    @JoinColumn(name = "fk_comment")
    private Comment commentRepliedTo;

    // Relacion 1:N con comentarios respuesta
    @OneToMany(mappedBy = "commentRepliedTo")
    // Coleccion de comentarios respuesta
    private Collection<Comment> commentReplies;
}
