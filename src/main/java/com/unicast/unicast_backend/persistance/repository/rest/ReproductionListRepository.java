package com.unicast.unicast_backend.persistance.repository.rest;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.ReproductionList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel = "reproductionLists", path = "reproductionLists")
public interface ReproductionListRepository extends JpaRepositoryExportedFalse<ReproductionList, Long> {
    public List<ReproductionList> findByName(String name);

    @RestResource(path = "user", rel = "user")
    @Query("select rl from ReproductionList rl where rl.user.id = ?#{ principal?.id }")
    public Page<ReproductionList> findByUserId(Pageable page);

    @Transactional
    @Modifying
    @RestResource(exported = false)
    default <S extends ReproductionList> S saveInternal(final S entity) {
      return save(entity);
    }

}