package com.unicast.unicast_backend.persistance.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.unicast.unicast_backend.persistance.model.Notification;

@RepositoryRestResource(collectionResourceRel = "notification", path = "notifications")
public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long> {

	List<Notification> findById(@Param("id") String id);
}