package com.unicast.unicast_backend.persistance.repository.rest;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.University;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel = "universities", path = "universities")
public interface UniversityRepository extends JpaRepositoryExportedFalse<University, Long> {

	@Override
    @RestResource(exported = true)
    Page<University> findAll(Pageable pageable);

    @PreAuthorize("hasAuthority('DELETE_UNIVERSITY_PRIVILEGE')")
    @Override
    @RestResource(exported = true)
    void deleteById(Long id);

    @PreAuthorize("hasAuthority('DELETE_UNIVERSITY_PRIVILEGE')")
    @Override
    @RestResource(exported = true)
    void delete(University subject);

    @Transactional
    @Modifying
    @RestResource(exported = false)
    default <S extends University> S saveInternal(final S entity) {
      return save(entity);
    }

    @PreAuthorize("hasAnyAuthority('CREATE_UNIVERSITY_PRIVILEGE, UPDATE_UNIVERSITY_PRIVILEGE')")
    @Override
    @RestResource(exported = true)
    <S extends University> S save(S entity);

    @RestResource(path = "nameStartsWith", rel = "nameStartsWith")
    public List<University> findByNameStartsWithIgnoreCase(@Param("name") String name);

    @RestResource(path = "nameContaining", rel = "nameContaining")
    public List<University> findByNameContainingIgnoreCase(@Param("name") String name);

    public University findByName(String name);
}