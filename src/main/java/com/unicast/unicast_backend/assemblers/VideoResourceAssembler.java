package com.unicast.unicast_backend.assemblers;

import com.unicast.unicast_backend.persistance.model.Video;
import com.unicast.unicast_backend.persistance.repository.rest.VideoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class VideoResourceAssembler implements ResourceAssembler<Video, Resource<Video>> {

    @Autowired
    private RepositoryEntityLinks entityLinks;

    @Override
    public Resource<Video> toResource(Video video) {
        // TODO: a;adir links a otros sitios
        return new Resource<>(video,
                entityLinks.linkToSingleResource(VideoRepository.class, video.getId()).withSelfRel());
    }
}