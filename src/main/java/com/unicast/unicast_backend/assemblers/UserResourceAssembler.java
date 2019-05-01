package com.unicast.unicast_backend.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.unicast.unicast_backend.controllers.UserController;
import com.unicast.unicast_backend.persistance.model.User;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, Resource<User>> {

    @Override
    public Resource<User> toResource(User user) {
        // TODO: a;adir links a otros sitios
        return new Resource<>(user, linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel());
    }
}