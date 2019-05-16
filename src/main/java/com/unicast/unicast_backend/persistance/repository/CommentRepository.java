package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;


@RepositoryRestResource(collectionResourceRel = "comments", path = "comments")
public interface CommentRepository extends JpaRepositoryExportedFalse<Comment, Long> {

    @RestResource(path = "video", rel = "video")
    public Page<Comment> findByVideoId(@Param("id") Long id, Pageable page);

}