package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Degree;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


// Comentario devuelto por el id 
@RepositoryRestResource(collectionResourceRel = "degrees", path = "degrees")
public interface DegreeRepository extends JpaRepository<Degree, Long> {
}