package com.unicast.unicast_backend.controllers;

import com.unicast.unicast_backend.assemblers.UserResourceAssembler;
import com.unicast.unicast_backend.configuration.SecurityConfiguration;
import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserResourceAssembler userAssembler;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @PostMapping(value = "/api/public/register")
    public Resource<User> registerNewUser(@RequestPart("user") User user) {

        // TODO: gestionar foto, descripcion, email etc, y comprobar que no haya un usuario con nombre/email iguales

        user.setPassword(securityConfiguration.passwordEncoder().encode(user.getPassword()));
        user.setEnabled(true);

        userRepository.save(user);
        return userAssembler.toResource(user);
    }
}   