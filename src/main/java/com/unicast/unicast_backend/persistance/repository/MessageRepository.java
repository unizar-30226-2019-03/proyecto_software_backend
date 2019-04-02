package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Message;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Message findByText(String text);
}