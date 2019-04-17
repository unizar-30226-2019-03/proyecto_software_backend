package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.University;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;


@RepositoryRestResource(collectionResourceRel = "university", path = "universities")
public interface UniversityRepository extends JpaRepository<University, Long>{
    University findByName(String name);
}