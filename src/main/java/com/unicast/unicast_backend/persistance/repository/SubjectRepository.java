package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "subject", path = "subjects")
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findByName(String name);
}