package com.unicast.unicast_backend.persistance.model;

import java.sql.Timestamp;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "commentWithUser", types = { Comment.class })
interface CommentWithUser {
    Long getId();

    String getText();

    Timestamp getTimestamp();

    Integer getSecondsFromBeginning();
    
    User getUser();
}