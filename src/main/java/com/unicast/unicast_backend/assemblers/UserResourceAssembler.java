package com.unicast.unicast_backend.assemblers;

import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.rest.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, Resource<User>> {

    @Autowired
    private RepositoryEntityLinks entityLinks;
    
    @Override
    public Resource<User> toResource(User user) {
        // TODO: a;adir links a otros sitios
        return new Resource<>(user, 
            entityLinks.linkToSingleResource(UserRepository.class, user.getId()).withSelfRel());
    }
}