package com.unicast.unicast_backend.persistance.repository.rest;

import com.unicast.unicast_backend.persistance.model.UserIsNotified;
import com.unicast.unicast_backend.persistance.model.UserIsNotifiedKey;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel = "usersAreNotified", path = "usersAreNotified")
public interface UserIsNotifiedRepository extends JpaRepositoryExportedFalse<UserIsNotified,UserIsNotifiedKey> {

    @RestResource(path = "userUncheckedNotifications", rel = "userUncheckedNotifications")
    @Query("select un from Notification n join UserIsNotified un on n = un.notification where un.user.id = ?#{ principal?.id } and un.checked = false order by n.timestamp desc")
    public Page<UserIsNotified> findByUsersIdInAndCheckedIsFalse(Pageable page);

    @RestResource(path = "userNotifications", rel = "userNotifications")
    @Query("select un from Notification n join UserIsNotified un on n = un.notification where un.user.id = ?#{ principal?.id } order by n.timestamp desc")
    public Page<UserIsNotified> findByUsersIdIn(Pageable page);

    @Transactional
    @Modifying
    @RestResource(exported = false)
    default <S extends UserIsNotified> S saveInternal(final S entity) {
      return save(entity);
    }
}
