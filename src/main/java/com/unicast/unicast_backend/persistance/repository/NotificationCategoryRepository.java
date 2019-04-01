package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.NotificationCategory;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "notificationCategory", path = "notificationCategory")
public interface NotificationCategoryRepository extends PagingAndSortingRepository<NotificationCategory, Long> {

	NotificationCategory findById(@Param("id") String id);

	NotificationCategory findByName(@Param("name") String name);
}