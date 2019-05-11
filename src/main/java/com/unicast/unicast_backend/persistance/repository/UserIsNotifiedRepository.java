package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.UserIsNotified;
import com.unicast.unicast_backend.persistance.model.UserIsNotifiedKey;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "usersAreNotified", path = "usersAreNotified")
public interface UserIsNotifiedRepository extends JpaRepository<UserIsNotified,UserIsNotifiedKey> {

    @RestResource(path = "userUncheckedNotifications", rel = "userUncheckedNotifications")
    @Query("select un from Notification n join UserIsNotified un on n = un.notification where un.user.id = ?1 and un.checked = false")
    public Page<UserIsNotified> findByUsersIdInAndCheckedIsFalse(@Param("user_id") Long userId, Pageable page);

    @RestResource(path = "userNotifications", rel = "userNotifications")
    @Query("select un from Notification n join UserIsNotified un on n = un.notification where un.user.id = ?1")
    public Page<UserIsNotified> findByUsersIdIn(@Param("user_id") Long userId, Pageable page);
}
