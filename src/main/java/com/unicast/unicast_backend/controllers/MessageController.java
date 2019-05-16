package com.unicast.unicast_backend.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.Instant;

import com.unicast.unicast_backend.assemblers.MessageResourceAssembler;
import com.unicast.unicast_backend.async.NotificationAsync;
import com.unicast.unicast_backend.persistance.model.Message;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.MessageRepository;
import com.unicast.unicast_backend.persistance.repository.UserRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
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

    @Autowired
    public MessageController(MessageRepository u, UserRepository a, MessageResourceAssembler messageAssembler,
            PagedResourcesAssembler<Message> pagedAssembler, NotificationAsync notificationAsync) {
        this.messageRepository = u;
        this.userRepository = a;
        this.messageAssembler = messageAssembler;
        this.pagedAssembler = pagedAssembler;
        this.notificationAsync = notificationAsync;
    }

    @PostMapping(value = "/api/messages", produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> addMessage(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("text") String text, @RequestParam("receiver_id") Long receiverId) throws URISyntaxException {
        User user = userAuth.getUser();

        Message message = new Message();

        User receiver = userRepository.findById(receiverId).get();

        // TODO: comprobar que receiver sea un profesor de una de las asignaturas del
        // usuario
        if ((user.getRole().equals("ROLE_USER") && receiver.getRole().equals("ROLE_PROFESSOR")) ||
            (user.getRole().equals("ROLE_PROFESSOR") && receiver.getRole().equals("ROLE_USER"))) {
                Timestamp now = Timestamp.from(Instant.now());
        
                message.setTimestamp(now);
                message.setText(text);
                message.setReceiver(receiver);
                message.setSender(user);
        
                messageRepository.save(message);
        
                notificationAsync.createUserNotificationsMessage(message, now);
        
                Resource<Message> messageResource = messageAssembler.toResource(message);
        
                return ResponseEntity.created(new URI(message.getId().toString())).body(messageResource);
        }

        // TODO: lanzar excepcion en condiciones
        throw new Error();
    }

    @GetMapping(value = "/api/messages", produces = "application/json")
    public ResponseEntity<?> getMessageFromSender(@AuthenticationPrincipal UserDetailsImpl userAuth,
            @RequestParam("sender_id") Long senderId, Pageable page) throws URISyntaxException {
        User user = userAuth.getUser();

        User sender = userRepository.findById(senderId).get();

        Page<Message> messagesPage = messageRepository.findByReceiverAndSender(user, sender, page);

        PagedResources<Resource<Message>> messagesResource = pagedAssembler.toResource(messagesPage);

        return ResponseEntity.ok(messagesResource);
    }
}
