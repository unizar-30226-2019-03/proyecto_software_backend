package com.unicast.unicast_backend.assemblers;

import com.unicast.unicast_backend.persistance.model.Video;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class VideoResourceAssembler implements ResourceAssembler<Video, Resource<Video>> {

    @Override
    public Resource<Video> toResource(Video video) {
        // TODO: a;adir links a otros sitios
        return new Resource<>(video);
    }
}