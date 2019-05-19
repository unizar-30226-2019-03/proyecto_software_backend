package com.unicast.unicast_backend.persistance.repository.rest;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.ReproductionList;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel = "reproductionLists", path = "reproductionLists")
public interface ReproductionListRepository extends JpaRepositoryExportedFalse<ReproductionList, Long> {
    public List<ReproductionList> findByName(String name);

    @RestResource(path = "user", rel = "user")
    @Query("select rl from ReproductionList rl where rl.user.id = ?#{ principal?.id }")
    public List<ReproductionList> findByUserId();

    @RestResource(path = "videoAndUser", rel = "videoAndUser")
    @Query("select rl from ReproductionList rl join Contains c on c.list = rl and c.video.id = ?1 where rl.user.id = ?#{ principal?.id } and c member of rl.videoList")
    public List<ReproductionList> findByUserIdAndVideoId(@Param("video_id") Long videoId);

    @Transactional
    @Modifying
    @RestResource(exported = false)
    default <S extends ReproductionList> S saveInternal(final S entity) {
      return save(entity);
    }

}