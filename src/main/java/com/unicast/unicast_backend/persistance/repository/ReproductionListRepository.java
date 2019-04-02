package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.ReproductionList;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReproductionListRepository extends JpaRepository<ReproductionList, Long> {
    ReproductionList findByName(String name);
}