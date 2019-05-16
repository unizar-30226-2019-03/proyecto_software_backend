package com.unicast.unicast_backend.persistance.repository;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.Vote;
import com.unicast.unicast_backend.persistance.model.VoteKey;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "votes", path = "votes")
public interface VoteRepository extends JpaRepositoryExportedFalse<Vote, VoteKey> {
    public List<Vote> findByVideoId(Long id);
}