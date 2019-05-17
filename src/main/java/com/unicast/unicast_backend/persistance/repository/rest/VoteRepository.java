package com.unicast.unicast_backend.persistance.repository.rest;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.Vote;
import com.unicast.unicast_backend.persistance.model.VoteKey;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel = "votes", path = "votes")
public interface VoteRepository extends JpaRepositoryExportedFalse<Vote, VoteKey> {
    public List<Vote> findByVideoId(Long id);

    @Transactional
    @Modifying
    @RestResource(exported = false)
    default <S extends Vote> S saveInternal(final S entity) {
      return save(entity);
    }
}