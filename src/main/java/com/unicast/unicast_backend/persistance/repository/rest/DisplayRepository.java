package com.unicast.unicast_backend.persistance.repository.rest;

import com.unicast.unicast_backend.persistance.model.Display;
import com.unicast.unicast_backend.persistance.model.DisplayKey;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel = "displays", path = "displays")
public interface DisplayRepository extends JpaRepositoryExportedFalse<Display, DisplayKey> {

    @RestResource(path = "user", rel = "user")
    @Query("select d from Display d where d.user.id = ?#{ principal?.id }")
    public Page<Display> findByUserId(Pageable page);

    @Transactional
    @Modifying
    @RestResource(exported = false)
    default <S extends Display> S saveInternal(final S entity) {
      return save(entity);
    }
}
