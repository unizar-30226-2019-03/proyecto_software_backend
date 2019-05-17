package com.unicast.unicast_backend.persistance.repository.rest;

import java.util.List;
import java.util.Optional;

import com.unicast.unicast_backend.persistance.model.Video;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel = "videos", path = "videos")
public interface VideoRepository extends JpaRepositoryExportedFalse<Video, Long> {

    @Override
    @RestResource(exported = true)
    Page<Video> findAll(Pageable pageable);

    @Override
    @RestResource(exported = true)
    Optional<Video> findById(Long id);

    @Transactional
    @Modifying
    @RestResource(exported = false)
    default <S extends Video> S saveInternal(final S entity) {
        return save(entity);
    }

    @RestResource(path = "title", rel = "title")
    public Video findByTitleIgnoreCase(@Param("title") String title);

    @RestResource(path = "titleStartsWith", rel = "titleStartsWith")
    public List<Video> findByTitleStartsWithIgnoreCase(@Param("title") String title);

    @RestResource(path = "titleContaining", rel = "titleContaining")
    public List<Video> findByTitleContainingIgnoreCase(@Param("title") String title);

    @RestResource(path = "uploaderVideos", rel = "uploaderVideos")
    @Query("select v from Video v where v.uploader.id = ?#{ principal?.id }")
    public Page<Video> findByUploaderId(Pageable page);

    @RestResource(path = "subjectVideos", rel = "subjectVideos")
    public Page<Video> findBySubjectId(@Param("subject_id") Long subjectId, Pageable page);

    @RestResource(path = "userSubjects", rel = "userSubjects")
    @Query("select v from Video v join Subject s on v.subject = s join User u on u.id = ?#{ principal?.id } where u member of s.followers")
    public Page<Video> findBySubjectsAndUserId(Pageable Page);

    @RestResource(path = "reproductionList", rel = "reproductionList")
    @Query("select c.video from Contains c where c.list.id = ?1 and c.list.user.id = ?#{ principal?.id }")
    public Page<Video> findByReproductionList(@Param("repro_list_id") Long reproListId, Pageable page);
}