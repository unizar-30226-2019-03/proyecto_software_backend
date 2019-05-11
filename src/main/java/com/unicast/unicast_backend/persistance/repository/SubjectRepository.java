package com.unicast.unicast_backend.persistance.repository;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.Subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "subjects", path = "subjects")
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @RestResource(path = "nameStartsWith", rel = "nameStartsWith")
    public List<Subject> findByNameStartsWithIgnoreCase(@Param("name") String name);

    @RestResource(path = "nameContaining", rel = "nameContaining")
    public List<Subject> findByNameContainingIgnoreCase(@Param("name") String name);

    @RestResource(path = "name", rel = "name")
    public Subject findByNameIgnoreCase(@Param("name") String name);

    @RestResource(path = "userIn", rel = "userIn")
    public boolean existsByIdAndUsersIdIn(@Param("subject_id") Long subjectId, @Param("user_id") Long userId);


}