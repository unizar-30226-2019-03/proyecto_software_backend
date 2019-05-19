package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Recommendation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
 
}