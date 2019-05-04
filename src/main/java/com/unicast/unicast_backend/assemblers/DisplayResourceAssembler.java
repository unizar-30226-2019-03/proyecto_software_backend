package com.unicast.unicast_backend.assemblers;

import com.unicast.unicast_backend.persistance.model.Display;
import com.unicast.unicast_backend.persistance.repository.DisplayRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class DisplayResourceAssembler implements ResourceAssembler<Display, Resource<Display>> {

    @Autowired
    private RepositoryEntityLinks entityLinks;

    @Override
    public Resource<Display> toResource(Display display) {
        // TODO: a;adir links a otros sitios
        return new Resource<>(display,
                entityLinks.linkToSingleResource(DisplayRepository.class, display.getId()).withSelfRel());
    }
}