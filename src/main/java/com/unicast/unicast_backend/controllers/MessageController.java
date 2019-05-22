package com.unicast.unicast_backend.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.unicast.unicast_backend.exceptions.NotProfessorReceiver;
import com.unicast.unicast_backend.exceptions.NotAdminSenderException; 
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

@RestController
public class MessageController {

    private final MessageRepository messageRepository;

    private final MessageResourceAssembler messageAssembler;

    private final UserRepository userRepository;

    private final PagedResourcesAssembler<Message> pagedAssembler;

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

    @PostMapping(value = "/api/messages", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> addMessage(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("text") String text, @RequestParam("receiver_id") Long receiverId) throws URISyntaxException,NotProfessorReceiver {
        User user = userAuth.getUser();

        Message message = new Message();

        User receiver = userRepository.findById(receiverId).get();

        // TODO: comprobar que receiver sea un profesor de una de las asignaturas del
        // usuario
        if ((user.getRole().equals("ROLE_USER") && receiver.getRole().equals("ROLE_PROFESSOR"))
                || (user.getRole().equals("ROLE_PROFESSOR") && receiver.getRole().equals("ROLE_USER"))) {
            Timestamp now = Timestamp.from(Instant.now());

            message.setTimestamp(now);
            message.setText(text);
            message.setReceiver(receiver);
            message.setSender(user);

            messageRepository.saveInternal(message);

            notificationAsync.createUserNotificationsMessage(message, now);

            Resource<Message> messageResource = messageAssembler.toResource(message);

            return ResponseEntity.created(new URI(message.getId().toString())).body(messageResource);
        }
        else {
            throw new NotProfessorReceiver("El usuario destinatario del mensaje no es un profesor");
        }
    
    }
    
    @GetMapping(value = "/api/messages/fromSender", produces = "application/json")
    public ResponseEntity<?> getMessageFromSender(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("sender_id") Long senderId, Pageable page) throws URISyntaxException {
        User user = userAuth.getUser();

        User sender = userRepository.findById(senderId).get();

        Page<Message> messagesPage = messageRepository.findByReceiverAndSender(user, sender, page);

        PagedResources<Resource<Message>> messagesResource = pagedAssembler.toResource(messagesPage);

        return ResponseEntity.ok(messagesResource);
    }

    @GetMapping(value = "/api/messages/toReceiver", produces = "application/json")
    public ResponseEntity<?> getMessagesSentTo(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("receiver_id") Long receiverId, Pageable page) throws URISyntaxException {
        User user = userAuth.getUser();

        User receiver = userRepository.findById(receiverId).get();

        Page<Message> messagesPage = messageRepository.findByReceiverAndSender(receiver, user, page);

        PagedResources<Resource<Message>> messagesResource = pagedAssembler.toResource(messagesPage);

        return ResponseEntity.ok(messagesResource);
    }

    @GetMapping(value = "/api/messages/lastMessages", produces = "application/json")
    public ResponseEntity<?> getLastMessages(@AuthenticationPrincipal UserDetailsImpl userAuth) 
        throws NotAdminSenderException{
        User user = userAuth.getUser();

        List<MessageWithReceiverAndSender> messages = new ArrayList<>();
        List<User> receivers = null;
        if (user.getRole().equals("ROLE_USER")) {
            receivers = userRepository.findProfessors(user);
        } 
        else if (user.getRole().equals("ROLE_PROFESSOR")) { 
            receivers = userRepository.findFollowersOfProfessorSubjects(user);
        }
        else if (user.getRole().equals("ROLE_ADMIN")){
            throw new NotAdminSenderException("El administrador puede mandar mensajes a ningun usuario");
        }

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

        messages.sort(new Comparator<MessageWithReceiverAndSender>() {
            @Override
            public int compare(MessageWithReceiverAndSender o1, MessageWithReceiverAndSender o2) {
                return o2.getTimestamp().compareTo(o1.getTimestamp());
            }
        });

        Resources<Resource<MessageWithReceiverAndSender>> messagesResource = Resources.wrap(messages);

        return ResponseEntity.ok(messagesResource);
    }
}
