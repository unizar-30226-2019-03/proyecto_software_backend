package com.unicast.unicast_backend.persistance.model;

import java.net.URI;
import java.sql.Timestamp;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "videoWithSubjectAndUniversity", types = { Video.class })
interface videoWithSubjectAndUniversity {
    Long getId();

    String getTitle();

    String getDescription();

    Timestamp getTimestamp();

    URI getUrl();

    URI getThumbnailUrl();

    Integer getSeconds();

    Subject getSubject();

    University getUniversity();

    Float getScore();
}