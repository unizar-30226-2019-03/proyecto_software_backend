package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Degree;

import org.springframework.data.jpa.repository.JpaRepository;


// Comentario devuelto por el id 
public interface DegreeRepository extends JpaRepository<Degree, Long> {
}