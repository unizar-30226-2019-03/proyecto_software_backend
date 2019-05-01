package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Display;
import com.unicast.unicast_backend.persistance.model.DisplayKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DisplayRepository extends JpaRepository<Display,DisplayKey> {
}