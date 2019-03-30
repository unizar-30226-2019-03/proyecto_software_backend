package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Video;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
 
    Video findById(String id);
    Video findByTitle(String title);
}