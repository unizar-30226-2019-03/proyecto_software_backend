/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

package com.unicast.unicast_backend.async;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.unicast.unicast_backend.persistance.model.Message;
import com.unicast.unicast_backend.persistance.model.Notification;
import com.unicast.unicast_backend.persistance.model.NotificationCategory;
import com.unicast.unicast_backend.persistance.model.Subject;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.model.UserIsNotified;
import com.unicast.unicast_backend.persistance.model.UserIsNotifiedKey;
import com.unicast.unicast_backend.persistance.repository.NotificationRepository;
import com.unicast.unicast_backend.persistance.repository.rest.NotificationCategoryRepository;
import com.unicast.unicast_backend.persistance.repository.rest.SubjectRepository;
import com.unicast.unicast_backend.persistance.repository.rest.UserIsNotifiedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/*
 * Gestion de notificaciones asincronas para el envio de 
 * mensajes entre los usuarios
 */

@Component
public class NotificationAsync {

    // Repositorio de las notificaciones
    @Autowired
    private NotificationRepository notificationRepository;

    // Repositorio de asignaturas
    @Autowired
    private SubjectRepository subjectRepository;

    // Repositorio para el control de notificaciones chequeadas
    @Autowired
    private UserIsNotifiedRepository userIsNotifiedRepository;

    // Repositorio para las categorias de las notificaciones
    @Autowired
    private NotificationCategoryRepository notificationCatRepository;

    /*
     * Permite crear la notificacion a enviar cuando un usuario 
     * sube un video a una asigntura
     * Parametros
     * @param subjectId: identificador de la asignatura del video a subir
     * @param now: timestamp del tiempo de subida de la notificacion
     */
    @Transactional
    @Async
    public CompletableFuture<Notification> createUserNotificationsVideo(Long subjectId, Timestamp now) {
        NotificationCategory notificationCat = notificationCatRepository.findByName("videos").get();

        // Creacion de la notificacion y asignacion de atributos
        Notification notification = new Notification();
        notification.setText("Notificacion de video");
        notification.setTimestamp(now);
        notification.setCategory(notificationCat);
        notification.setCreatorId(subjectId);

        // Guardar la notificacion creada en el repositorio
        notificationRepository.save(notification);

        // Obtencion de la asignatura en el repositorio por su id
        List<UserIsNotified> usersNotifications = new ArrayList<>();
        Subject subject = subjectRepository.findById(subjectId).get();

        // Para cada usuario de la lista de seguidores de la asignatura se crea una notificacion
        for (User user : subject.getFollowers()) {
            UserIsNotifiedKey userIsNotifiedKey = new UserIsNotifiedKey();
            userIsNotifiedKey.setNotificationId(notification.getId());
            userIsNotifiedKey.setUserId(user.getId());
            UserIsNotified userIsNotified = new UserIsNotified();
            userIsNotified.setId(userIsNotifiedKey);
            userIsNotified.setChecked(false);
            usersNotifications.add(userIsNotified);
        }

        // Guardar los cambios en el repositorio de notificaciones 
        userIsNotifiedRepository.saveAll(usersNotifications);

        // Respuesta de la aplicacion
        return CompletableFuture.completedFuture(notification);
    }

    /*
     * Permite crear el mensaje de una notificacion cuando se sube un video
     * Parametros
     * @param message: contenido del mensaje a enviar
     * @param now: estampilla con el momento de subida del video
     */
    @Transactional
    @Async
    public CompletableFuture<Notification> createUserNotificationsMessage(Message message, Timestamp now) {

        // Obtencion de la categoria de la notificacion
        NotificationCategory notificationCat = notificationCatRepository.findByName("messages").get();

        // Creacion de la nueva notificacion del mensaje
        Notification notification = new Notification();
        notification.setText("Notificacion de mensaje");
        notification.setTimestamp(now);
        notification.setCategory(notificationCat);
        notification.setCreatorId(message.getSender().getId());

        // Guardar notificacion en el repositorio
        notificationRepository.save(notification);

        // Asignacion de atributos y poner por defecto que la notificacion no ha sido
        // chequeada por el usuario
        UserIsNotifiedKey userIsNotifiedKey = new UserIsNotifiedKey();
        userIsNotifiedKey.setNotificationId(notification.getId());
        userIsNotifiedKey.setUserId(message.getReceiver().getId());
        UserIsNotified userIsNotified = new UserIsNotified();
        userIsNotified.setId(userIsNotifiedKey);
        userIsNotified.setChecked(false);
        userIsNotifiedRepository.saveInternal(userIsNotified);

        // Respuesta de la operacion
        return CompletableFuture.completedFuture(notification);
    }
    
}
