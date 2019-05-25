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
 * Relacion entre los usuarios y las notificaciones que han recibido
 */

@Data
@Entity
@Table(name = "notification_app_user")
public class UserIsNotified {
    
    // Identificador de la notificacion
	@EmbeddedId
    private UserIsNotifiedKey id;
    
    // Relacion N:1 con notificacion
	@ManyToOne
    @MapsId("fk_notification")
    @JoinColumn(name = "fk_notification")
    // Notificacion recibida
    private Notification notification;
    
    // Relacion N:1 con usuario
	@ManyToOne
    @MapsId("fk_app_user")
    @JoinColumn(name = "fk_app_user")
    // Usuario receptor de la notificacion
    private User user;
    
    // Control de si la notificacion la ha visualizado el usuario o no
	private boolean checked;
	
	
}
