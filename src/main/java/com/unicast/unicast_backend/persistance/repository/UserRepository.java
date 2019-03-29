package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
 
    User findByUsername(String username);
}
