package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Override
    void delete(Role role);

}