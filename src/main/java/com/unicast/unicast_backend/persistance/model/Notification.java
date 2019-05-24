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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

/*
 * Entidad notificacion
 */

@Data
@Entity
@Table(name = "notification")
public class Notification {

    // Identificador de la notificacion
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Contenido de la notificacion
    private String text;

    // Identificador del usuario creador
    private Long creatorId;

    // Estampilla temporal del momento de creacion
    private Timestamp timestamp;

    // Relacion N:1 con categoria de notificacion
    @ManyToOne
    @JoinColumn(name = "fk_category")
    // Categoria de la notificacion
    private NotificationCategory category;

    // Coleccion de usuarios que reciben  notificacion
    @OneToMany(mappedBy = "notification")
    private Collection<UserIsNotified> users;

    /*
     * Permite obtener la categoria de una notificacion
     */
	@JsonProperty(access = Access.READ_ONLY)
    public String getNotificationCategory() {
        return category.getName();
    }
}
