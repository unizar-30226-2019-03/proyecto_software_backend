package com.unicast.unicast_backend.persistance.repository;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.Video;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "videos", path = "videos")
public interface VideoRepository extends JpaRepository<Video, Long> {
    @RestResource(path = "title", rel = "title")
    public Video findByTitle(@Param("title") String title);

    @RestResource(path = "titleStartsWith", rel = "titleStartsWith")
    public List<Video> findByTitleStartsWith(@Param("title") String title);

    @RestResource(path = "titleContaining", rel = "titleContaining")
    public List<Video> findByTitleContaining(@Param("title") String title);

    @RestResource(path = "uploaderVideos", rel = "uploaderVideos")
    public List<Video> findByUploaderId(@Param("uploader_id") Long uploaderId, Pageable page);

    @RestResource(path = "subjectVideos", rel = "subjectVideos")
    public List<Video> findBySubjectId(@Param("subject_id") Long subject_id, Pageable page);
}