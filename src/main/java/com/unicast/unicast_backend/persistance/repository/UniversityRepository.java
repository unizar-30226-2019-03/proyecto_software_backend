package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.University;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {
    University findById(String id);
    University findByName(String name);
}