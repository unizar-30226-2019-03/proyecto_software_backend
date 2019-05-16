package com.unicast.unicast_backend.persistance.repository;

import com.unicast.unicast_backend.persistance.model.Display;
import com.unicast.unicast_backend.persistance.model.DisplayKey;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "displays", path = "displays")
public interface DisplayRepository extends JpaRepositoryExportedFalse<Display, DisplayKey> {

    @RestResource(path = "user", rel = "user")
    @Query("select d from Display d where d.user.id = ?#{ principal?.id }")
    public Page<Display> findByUserId(@Param("id") Long id, Pageable page);
}