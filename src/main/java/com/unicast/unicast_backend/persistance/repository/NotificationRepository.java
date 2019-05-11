package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Notification;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
