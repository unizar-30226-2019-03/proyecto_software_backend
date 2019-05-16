package com.unicast.unicast_backend.persistance.repository;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.University;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource(collectionResourceRel = "universities", path = "universities")
public interface UniversityRepository extends JpaRepositoryExportedFalse<University, Long> {

    @PreAuthorize("hasAuthority('DELETE_UNIVERSITY_PRIVILEGE')")
    @Override
    void deleteById(Long id);

    @PreAuthorize("hasAuthority('DELETE_UNIVERSITY_PRIVILEGE')")
    @Override
    void delete(University subject);

    @PreAuthorize("hasAnyAuthority('CREATE_UNIVERSITY_PRIVILEGE, UPDATE_UNIVERSITY_PRIVILEGE')")
    @Override
    <S extends University> S save(S entity);

    @RestResource(path = "nameStartsWith", rel = "nameStartsWith")
    public List<University> findByNameStartsWithIgnoreCase(@Param("name") String name);

    @RestResource(path = "nameContaining", rel = "nameContaining")
    public List<University> findByNameContainingIgnoreCase(@Param("name") String name);

    public University findByName(String name);
}