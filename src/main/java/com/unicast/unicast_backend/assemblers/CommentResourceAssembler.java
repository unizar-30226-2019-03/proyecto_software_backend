package com.unicast.unicast_backend.assemblers;

import com.unicast.unicast_backend.persistance.model.Comment;
import com.unicast.unicast_backend.persistance.repository.rest.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class CommentResourceAssembler implements ResourceAssembler<Comment, Resource<Comment>> {

    @Autowired
    private RepositoryEntityLinks entityLinks;

    @Override
    public Resource<Comment> toResource(Comment comment) {
        // TODO: a;adir links a otros sitios
        return new Resource<>(comment,
                entityLinks.linkToSingleResource(CommentRepository.class, comment.getId()).withSelfRel());
    }
}