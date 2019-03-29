package com.unicast.unicast_backend.repository;

import com.unicast.unicast_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
 
    User findByUsername(String username);
}
