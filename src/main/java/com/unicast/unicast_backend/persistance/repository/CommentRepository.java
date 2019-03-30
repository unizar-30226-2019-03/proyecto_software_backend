package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
 
    Comment findById(String Id);
}