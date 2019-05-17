package com.unicast.unicast_backend.persistance.repository.rest;

import java.util.Optional;

import com.unicast.unicast_backend.persistance.model.NotificationCategory;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel = "notificationCategory", path = "notificationCategory")
public interface NotificationCategoryRepository extends JpaRepositoryExportedFalse<NotificationCategory, Long> {

    Optional<NotificationCategory> findByName(@Param("name") String name);

    @Transactional
    @Modifying
    @RestResource(exported = false)
    default <S extends NotificationCategory> S saveInternal(final S entity) {
      return save(entity);
    }
}