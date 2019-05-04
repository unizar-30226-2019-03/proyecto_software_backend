package com.unicast.unicast_backend.persistance.repository;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.Video;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "videos", path = "videos")
public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findByTitle(String title);

    @RestResource(path = "titleStartsWith", rel = "titleStartsWith")
    public List<Video> findByTitleStartsWith(@Param("title") String title);

    @RestResource(path = "titleContaining", rel = "titleContaining")
    public List<Video> findByTitleContaining(@Param("title") String title);
}