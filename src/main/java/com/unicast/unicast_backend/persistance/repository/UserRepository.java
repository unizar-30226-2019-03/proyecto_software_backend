package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends JpaRepository<User, Long> {
 
    User findByUsername(String username);
}
