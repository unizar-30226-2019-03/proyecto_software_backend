package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Vote;
import com.unicast.unicast_backend.persistance.model.VoteKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote,VoteKey> {
}