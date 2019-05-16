package com.unicast.unicast_backend.persistance.repository;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.Degree;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;


// Comentario devuelto por el id 
@RepositoryRestResource(collectionResourceRel = "degrees", path = "degrees")
public interface DegreeRepository extends JpaRepositoryExportedFalse<Degree, Long> {
    
    @PreAuthorize("hasAuthority('DELETE_DEGREE_PRIVILEGE')")
    @Override
    void deleteById(Long id);

    @PreAuthorize("hasAuthority('DELETE_DEGREE_PRIVILEGE')")
    @Override
    void delete(Degree subject);

    @PreAuthorize("hasAnyAuthority('CREATE_DEGREE_PRIVILEGE, UPDATE_DEGREE_PRIVILEGE')")
    @Override
    <S extends Degree> S save(S entity);

    @RestResource(path = "nameStartsWith", rel = "nameStartsWith")
    public List<Degree> findByNameStartsWithIgnoreCase(@Param("name") String name);

    @RestResource(path = "nameContaining", rel = "nameContaining")
    public List<Degree> findByNameContainingIgnoreCase(@Param("name") String name);

    @RestResource(path = "name", rel = "name")
    public Degree findByNameIgnoreCase(@Param("name") String name);

}