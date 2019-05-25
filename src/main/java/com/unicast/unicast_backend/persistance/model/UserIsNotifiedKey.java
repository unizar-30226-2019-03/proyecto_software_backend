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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/*
 * Relacion N:M entre usuarios y notificaciones recibidas
 */

@Data
@Embeddable
public class UserIsNotifiedKey implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Identificador de la notificacion
	@Column(name = "fk_notification")
    private Long notificationId;
    
    // Identificador del usuario
	@Column(name = "fk_app_user")
    private Long userId;
	
}


