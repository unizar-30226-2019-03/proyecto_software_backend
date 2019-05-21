package com.unicast.unicast_backend.persistance.repository.rest;

import com.unicast.unicast_backend.persistance.model.Message;
import com.unicast.unicast_backend.persistance.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel = "messages", path = "messages")
public interface MessageRepository extends JpaRepositoryExportedFalse<Message, Long> {
    public Message findTopByReceiverAndSenderOrderByTimestampDesc(User receiver, User sender);

    public Page<Message> findByReceiverAndSender(User receiver, User sender, Pageable page);

    @Transactional
    @Modifying
    @RestResource(exported = false)
    default <S extends Message> S saveInternal(final S entity) {
      return save(entity);
    }
}
