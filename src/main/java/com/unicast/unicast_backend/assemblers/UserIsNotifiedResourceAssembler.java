package com.unicast.unicast_backend.assemblers;

import com.unicast.unicast_backend.persistance.model.UserIsNotified;
import com.unicast.unicast_backend.persistance.repository.rest.UserIsNotifiedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserIsNotifiedResourceAssembler implements ResourceAssembler<UserIsNotified, Resource<UserIsNotified>> {

    @Autowired
    private RepositoryEntityLinks entityLinks;

    @Override
    public Resource<UserIsNotified> toResource(UserIsNotified userIsNotified) {
        // TODO: a;adir links a otros sitios
        return new Resource<>(userIsNotified,
                entityLinks.linkToSingleResource(UserIsNotifiedRepository.class, userIsNotified.getId()).withSelfRel());
    }
}