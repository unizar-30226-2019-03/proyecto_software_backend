package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Subject;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findById(String id);
    Subject findByName(String name);
}