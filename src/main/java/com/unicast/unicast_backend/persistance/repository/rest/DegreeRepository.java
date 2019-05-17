package com.unicast.unicast_backend.persistance.repository.rest;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.Degree;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;


// Comentario devuelto por el id 
@RepositoryRestResource(collectionResourceRel = "degrees", path = "degrees")
public interface DegreeRepository extends JpaRepositoryExportedFalse<Degree, Long> {
    
    @Override
    @RestResource(exported = true)
    Page<Degree> findAll(Pageable pageable);

    @PreAuthorize("hasAuthority('DELETE_DEGREE_PRIVILEGE')")
    @Override
    @RestResource(exported = true)
    void deleteById(Long id);

    @PreAuthorize("hasAuthority('DELETE_DEGREE_PRIVILEGE')")
    @Override
    @RestResource(exported = true)
    void delete(Degree subject);

    @Transactional
    @Modifying
    @RestResource(exported = false)
    default <S extends Degree> S saveInternal(final S entity) {
      return save(entity);
    }

    @PreAuthorize("hasAnyAuthority('CREATE_DEGREE_PRIVILEGE, UPDATE_DEGREE_PRIVILEGE')")
    @Override
    @RestResource(exported = true)
    <S extends Degree> S save(S entity);

    @RestResource(path = "nameStartsWith", rel = "nameStartsWith")
    public List<Degree> findByNameStartsWithIgnoreCase(@Param("name") String name);

    @RestResource(path = "nameContaining", rel = "nameContaining")
    public List<Degree> findByNameContainingIgnoreCase(@Param("name") String name);

    @RestResource(path = "name", rel = "name")
    public Degree findByNameIgnoreCase(@Param("name") String name);

}