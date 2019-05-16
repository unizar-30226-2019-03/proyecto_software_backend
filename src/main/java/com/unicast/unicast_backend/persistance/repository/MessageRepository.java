package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Message;
import com.unicast.unicast_backend.persistance.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "messages", path = "messages")
public interface MessageRepository extends JpaRepositoryExportedFalse<Message, Long> {
    public Page<Message> findByReceiverAndSender(User receiver, User sender, Pageable page);
}
