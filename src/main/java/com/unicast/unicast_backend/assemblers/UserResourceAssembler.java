package com.unicast.unicast_backend.assemblers;

import com.unicast.unicast_backend.persistance.model.User;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, Resource<User>> {

    @Override
    public Resource<User> toResource(User user) {
        // TODO: a;adir links a otros sitios
        return new Resource<>(user);
    }
}