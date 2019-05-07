package com.unicast.unicast_backend.persistance.repository;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.ReproductionList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "reproductionLists", path = "reproductionLists")
public interface ReproductionListRepository extends JpaRepository<ReproductionList, Long> {
    List<ReproductionList> findByName(String name);
}