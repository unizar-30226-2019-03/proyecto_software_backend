package com.unicast.unicast_backend.persistance.model;

import java.sql.Timestamp;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "displayWithVideo", types = { Display.class })
interface DisplayWithVideo {
    DisplayKey getId();

    Video getVideo();

    Timestamp getTimestampLastTime();

    Integer getSecsFromBeg();
}