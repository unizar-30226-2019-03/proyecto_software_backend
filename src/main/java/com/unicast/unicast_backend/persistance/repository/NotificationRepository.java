/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/


package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Notification;

import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Repositorio JPA para gestionar las notificaciones enviadas a los usuarios
 */

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
