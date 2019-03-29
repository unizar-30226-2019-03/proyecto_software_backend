package com.unicast.unicast_backend.persistance.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.unicast.unicast_backend.persistance.model.NotificationCategory;

@RepositoryRestResource(collectionResourceRel = "notificationCategory", path = "notificationCategory")
public interface NotificationCategoryRepository extends PagingAndSortingRepository<NotificationCategory, Long> {

	List<NotificationCategory> findById(@Param("id") String id);
}