package com.unicast.unicast_backend.persistance.model;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "subjectWithUniversity", types = { Subject.class })
interface SubjectWithUniversity {
    Long getId();

    String getName();

    String getAbbreviation();

    Double getPoints();

    University getUniversity();
}