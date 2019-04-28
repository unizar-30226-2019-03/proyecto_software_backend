package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "video", path = "videos")
public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findByTitle(String title);
}