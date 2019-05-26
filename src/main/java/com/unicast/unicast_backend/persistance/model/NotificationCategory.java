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

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

/*
 * Categoria de una notificacion 
 */

@Data
@Entity
@Table(name = "notification_category")
public class NotificationCategory {
   
    // Identificador de una categoria
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de la categoria
    private String name;

    // Coleccion de notificaciones de un tipo de categoria
    @OneToMany(mappedBy = "category")
    private Collection<Notification> notifications;

}
