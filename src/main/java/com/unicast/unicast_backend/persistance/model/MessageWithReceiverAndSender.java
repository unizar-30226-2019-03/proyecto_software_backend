package com.unicast.unicast_backend.persistance.model;

import java.sql.Timestamp;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "messageWithReceiverAndSender", types = { Message.class })
public interface MessageWithReceiverAndSender {
    Long getId();
    
    String getText();

    Timestamp getTimestamp();

    User getReceiver();

    User getSender();
}
