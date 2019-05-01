package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Contains;
import com.unicast.unicast_backend.persistance.model.ContainsKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContainsRepository extends JpaRepository<Contains,ContainsKey> {
}