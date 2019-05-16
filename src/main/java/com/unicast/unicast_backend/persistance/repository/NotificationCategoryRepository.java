package com.unicast.unicast_backend.persistance.repository;

import java.util.Optional;

import com.unicast.unicast_backend.persistance.model.NotificationCategory;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "notificationCategory", path = "notificationCategory")
public interface NotificationCategoryRepository extends JpaRepositoryExportedFalse<NotificationCategory, Long> {

	Optional<NotificationCategory> findByName(@Param("name") String name);
}