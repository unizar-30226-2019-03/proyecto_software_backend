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

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.unicast.unicast_backend.exceptions.NotAdminSenderException;
import com.unicast.unicast_backend.exceptions.NotProfessorReceiverException;
import com.unicast.unicast_backend.assemblers.MessageResourceAssembler;
import com.unicast.unicast_backend.async.NotificationAsync;
import com.unicast.unicast_backend.persistance.model.Message;
import com.unicast.unicast_backend.persistance.model.MessageWithReceiverAndSender;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.rest.MessageRepository;
import com.unicast.unicast_backend.persistance.repository.rest.UserRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * Controlador REST de los mensajes 
 */

@RestController
public class MessageController {

    // Repositorio para los mensajes
    private final MessageRepository messageRepository;

    // Ensamblador de mensajes
    private final MessageResourceAssembler messageAssembler;

    // Repositorio para los usuarios
    private final UserRepository userRepository;

    // Paginas ensambladas
    private final PagedResourcesAssembler<Message> pagedAssembler;

    // Control de notificaciones asincronas
    private final NotificationAsync notificationAsync;

    private final ProjectionFactory projectionFactory;

    @Autowired
    public MessageController(MessageRepository u, UserRepository a, MessageResourceAssembler messageAssembler,
            PagedResourcesAssembler<Message> pagedAssembler, NotificationAsync notificationAsync,
            ProjectionFactory projectionFactory) {
        this.messageRepository = u;
        this.userRepository = a;
        this.messageAssembler = messageAssembler;
        this.pagedAssembler = pagedAssembler;
        this.notificationAsync = notificationAsync;
        this.projectionFactory = projectionFactory;
    }

    /*
     * Permite anyadir un mensaje a enviar a un profesor
     * Parametros:
     *  @param userAuth: token con los datos del usuario loggeado
     *  @param text: texto del mensaje a enviar
     *  @param receiverId: codigo identificador del destinatario
     */
    @PostMapping(value = "/api/messages", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> addMessage(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("text") String text, @RequestParam("receiver_id") Long receiverId) 
            throws URISyntaxException, NotProfessorReceiverException {
       
        // Extraccion de los datos de usuario
        User user = userAuth.getUser();

        // Creacion del mensaje
        Message message = new Message();

        // Obtencion del usuario destinatario del repositorio a traves del id
        User receiver = userRepository.findById(receiverId).get();

        // comprobar que receiver sea un profesor de una de las asignaturas del
        // usuario
        if ((user.getRole().equals("ROLE_USER") && receiver.getRole().equals("ROLE_PROFESSOR"))
                || (user.getRole().equals("ROLE_PROFESSOR") && receiver.getRole().equals("ROLE_USER"))) {
            
            // Insercion de la estampilla temporar
            Timestamp now = Timestamp.from(Instant.now());

            // Anyadir atributos al mensaje
            message.setTimestamp(now);
            message.setText(text);
            message.setReceiver(receiver);
            message.setSender(user);

            // Guardar cambios en el repositorio
            messageRepository.saveInternal(message);

            // Creacion de la notificacion asincrona
            notificationAsync.createUserNotificationsMessage(message, now);

            Resource<Message> messageResource = messageAssembler.toResource(message);

            // Mandar respuesta de la operacion
            return ResponseEntity.created(new URI(message.getId().toString())).body(messageResource);
        }
        else {
            // Excepcion para control de envio de mensajes a profesores
            // throw new NotProfessorReceiverException("El usuario que recibe el mensaje debe ser profesor");
            throw new NotProfessorReceiverException("El administrador no puede mandar mensajes a ningun usuario");
        }
    
    }
    
    /*
     * Permite obtener el mensaje enviado por un usuario
     * Parametros:
     *  @param userAuth: token con los datos del usuario loggeado
     *  @param senderId: identificador del usuario remitente
     *  @param page: pagina del mensaje
     */
    @GetMapping(value = "/api/messages/fromSender", produces = "application/json")
    public ResponseEntity<?> getMessageFromSender(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("sender_id") Long senderId, Pageable page) throws URISyntaxException {
        
        // Extraccion de los datos del usuario
        User user = userAuth.getUser();

        // Encontrar usuario remitente en el repositorio por su id
        User sender = userRepository.findById(senderId).get();

        // Encontrar el mensaje recibido
        Page<Message> messagesPage = messageRepository.findByReceiverAndSender(user, sender, page);

        // Respuesta de la operacion
        PagedResources<Resource<Message>> messagesResource = pagedAssembler.toResource(messagesPage);
        return ResponseEntity.ok(messagesResource);
    }


    /*
     * Permite obtener el mensaje enviado a un usuario concreto
     * Parametros:
     *  @param userAuth: token con los datos del usuario loggeado
     *  @param senderId: identificador del usuario remitente
     *  @param page: pagina del mensaje
     */
    @GetMapping(value = "/api/messages/toReceiver", produces = "application/json")
    public ResponseEntity<?> getMessagesSentTo(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("receiver_id") Long receiverId, Pageable page) throws URISyntaxException {

        // Extraccion de los datos del usuario
        User user = userAuth.getUser();

        // Encontrar usuario destinatario en el repositorio por su id
        User receiver = userRepository.findById(receiverId).get();

        // Encontrar el mensaje enviado
        Page<Message> messagesPage = messageRepository.findByReceiverAndSender(receiver, user, page);

        // Respuesta de la operacion
        PagedResources<Resource<Message>> messagesResource = pagedAssembler.toResource(messagesPage);
        return ResponseEntity.ok(messagesResource);
    }

    
    /*
     * Permite obtener un listado de los ultimos mensajes con profesores / alumnos
     * segun quien el tipo de usuario que realiza la invocacion
     * Parametros:
     *  @param userAuth: token con los datos del usuario loggeado
     */
    @GetMapping(value = "/api/messages/lastMessages", produces = "application/json")
    public ResponseEntity<?> getLastMessages(@AuthenticationPrincipal UserDetailsImpl userAuth) 
        throws NotAdminSenderException{
        
        // Extraccion de los datos del usuario
        User user = userAuth.getUser();

        // Lista de mensajes
        List<MessageWithReceiverAndSender> messages = new ArrayList<>();
        List<User> receivers = null;


        if (user.getRole().equals("ROLE_USER")) {
            // Busqueda de los usuarios destinatarios si es alumno
            receivers = userRepository.findProfessors(user);
        } 
        else if (user.getRole().equals("ROLE_PROFESSOR")) { 
            // Busqueda de los usuarios destinatarios si es profesor
            receivers = userRepository.findFollowersOfProfessorSubjects(user);
        }
        else if (user.getRole().equals("ROLE_ADMIN")){
            // Control de excepcion dado que los administradores no pueden mandar mendajes
            throw new NotAdminSenderException("El administrador no puede mandar mensajes a ningun usuario");
        }

        // Obtencion de los mensajes en una lista
        for (User receiver : receivers) {
            Message message1 = messageRepository.findTop1ByReceiverAndSenderOrderByTimestampDesc(receiver, user);
            Message message2 = messageRepository.findTop1ByReceiverAndSenderOrderByTimestampDesc(user, receiver);
            if (message1 != null && message2 != null) {
                if (message1.getTimestamp().after(message2.getTimestamp())) {
                    messages.add(projectionFactory.createProjection(MessageWithReceiverAndSender.class, message1));
                } else {
                    messages.add(projectionFactory.createProjection(MessageWithReceiverAndSender.class, message2));
                }
            } else if (message1 != null) {
                messages.add(projectionFactory.createProjection(MessageWithReceiverAndSender.class, message1));
            } else if (message2 != null) {
                messages.add(projectionFactory.createProjection(MessageWithReceiverAndSender.class, message2));
            }
        }

        // Ordenacion de los mensajes por valor de estampilla de tiempo
        messages.sort(new Comparator<MessageWithReceiverAndSender>() {
            @Override
            public int compare(MessageWithReceiverAndSender o1, MessageWithReceiverAndSender o2) {
                return o2.getTimestamp().compareTo(o1.getTimestamp());
            }
        });

        // Respuesta de la operacion
        Resources<Resource<MessageWithReceiverAndSender>> messagesResource = Resources.wrap(messages);
        return ResponseEntity.ok(messagesResource);
    }
}
