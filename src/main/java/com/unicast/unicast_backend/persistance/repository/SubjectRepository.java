package com.unicast.unicast_backend.persistance.repository;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.Subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "subjects", path = "subjects")
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findByName(String name);

    @RestResource(path = "nameStartsWith", rel = "nameStartsWith")
    public List<Subject> findByNameStartsWith(@Param("name") String name);

    @RestResource(path = "nameContaining", rel = "nameContaining")
    public List<Subject> findByNameContaining(@Param("name") String name);
}