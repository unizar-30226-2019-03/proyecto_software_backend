package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Privilege;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(String name);

}