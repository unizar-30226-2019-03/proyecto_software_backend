package com.unicast.unicast_backend.persistance.repository;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.Degree;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;


// Comentario devuelto por el id 
@RepositoryRestResource(collectionResourceRel = "degrees", path = "degrees")
public interface DegreeRepository extends JpaRepository<Degree, Long> {
    @RestResource(path = "nameStartsWith", rel = "nameStartsWith")
    public List<Degree> findByNameStartsWith(@Param("name") String name);

    @RestResource(path = "nameContaining", rel = "nameContaining")
    public List<Degree> findByNameContaining(@Param("name") String name);
}