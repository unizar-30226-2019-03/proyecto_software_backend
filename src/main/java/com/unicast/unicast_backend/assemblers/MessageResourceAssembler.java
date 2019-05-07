package com.unicast.unicast_backend.assemblers;

import com.unicast.unicast_backend.persistance.model.Message;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class MessageResourceAssembler implements ResourceAssembler<Message, Resource<Message>> {

    @Override
    public Resource<Message> toResource(Message message) {
        // TODO: a;adir links a otros sitios
        return new Resource<>(message);
    }
}