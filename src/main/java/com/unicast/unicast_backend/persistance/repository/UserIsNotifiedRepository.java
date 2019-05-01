package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.UserIsNotified;
import com.unicast.unicast_backend.persistance.model.UserIsNotifiedKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIsNotifiedRepository extends JpaRepository<UserIsNotified,UserIsNotifiedKey> {
}