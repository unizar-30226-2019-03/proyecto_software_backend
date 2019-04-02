package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.VideoTag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoTagRepository extends JpaRepository<VideoTag, Long> {
 
    VideoTag findByName(String name);
}