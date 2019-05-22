/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

 package com.unicast.unicast_backend.controllers;

import com.unicast.unicast_backend.assemblers.UserIsNotifiedResourceAssembler;
import com.unicast.unicast_backend.persistance.model.UserIsNotified;
import com.unicast.unicast_backend.persistance.model.UserIsNotifiedKey;
import com.unicast.unicast_backend.persistance.repository.rest.UserIsNotifiedRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * Controlador REST de notifaciones de usuarios
 */

@RestController
public class UserIsNotifiedController {

    // Repositorio de notificaciones de usuarios
    private final UserIsNotifiedRepository userIsNotifiedRepository;

    // Ensamblador
    private final UserIsNotifiedResourceAssembler userIsNotifiedAssembler;


    /*
     * Constructor de controlador REST
     */
    @Autowired
    public UserIsNotifiedController(UserIsNotifiedRepository userIsNotifiedRepository,
            UserIsNotifiedResourceAssembler userIsNotifiedAssembler) {
        this.userIsNotifiedRepository = userIsNotifiedRepository;
        this.userIsNotifiedAssembler = userIsNotifiedAssembler;
    }


    /*
     * Permite chequear la visualizacion de una notificacion de un usuario
     * @param userAuth: token con los datos del usuario loggeado
     * @param notificationId: identificador de la notificacion a chequear
     */
    @PatchMapping("/api/usersAreNotified/check")
    public ResponseEntity<?> checkNotification(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("notification_id") Long notificationId) {

        // Creacion de la notificacion a comprobar
        UserIsNotifiedKey userIsNotifiedKey = new UserIsNotifiedKey();
        userIsNotifiedKey.setNotificationId(notificationId);
        userIsNotifiedKey.setUserId(userAuth.getUser().getId());

        // Buscar la notificacion por id en el repositorio
        UserIsNotified userIsNotified = userIsNotifiedRepository.findById(userIsNotifiedKey).get();

        // Actualizar que ha sido visualizada y guardar cambios en el repositorio
        userIsNotified.setChecked(true);
        userIsNotifiedRepository.saveInternal(userIsNotified);

        // Envio de respuesta de la operacion
        Resource<UserIsNotified> resourceUserIsNotified = userIsNotifiedAssembler.toResource(userIsNotified);
        return ResponseEntity.ok(resourceUserIsNotified);
    }
}