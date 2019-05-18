package com.unicast.unicast_backend.persistance.repository.rest;

import java.util.List;
import java.util.Optional;

import com.unicast.unicast_backend.persistance.model.Subject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel = "subjects", path = "subjects")
public interface SubjectRepository extends JpaRepositoryExportedFalse<Subject, Long> {

    @Override
    @RestResource(exported = true)
    Page<Subject> findAll(Pageable pageable);

    @PreAuthorize("hasAuthority('DELETE_SUBJECT_PRIVILEGE')")
    @Override
    @RestResource(exported = true)
    void deleteById(Long id);

    @PreAuthorize("hasAuthority('DELETE_SUBJECT_PRIVILEGE')")
    @Override
    @RestResource(exported = true)
    void delete(Subject subject);

    @Override
    @RestResource(exported = true)
    Optional<Subject> findById(Long id);

    @PreAuthorize("hasAnyAuthority('CREATE_SUBJECT_PRIVILEGE, UPDATE_SUBJECT_PRIVILEGE')")
    @Override
    @RestResource(exported = true)
    <S extends Subject> S save(S entity);

    @Transactional
    @Modifying
    @RestResource(exported = false)
    default <S extends Subject> S saveInternal(final S entity) {
        return save(entity);
    }

    @RestResource(path = "nameStartsWith", rel = "nameStartsWith")
    public List<Subject> findByNameStartsWithIgnoreCase(@Param("name") String name);

    @RestResource(path = "nameContaining", rel = "nameContaining")
    public List<Subject> findByNameContainingIgnoreCase(@Param("name") String name);

    @RestResource(path = "name", rel = "name")
    public List<Subject> findByNameIgnoreCase(@Param("name") String name);

    @RestResource(path = "ranking", rel = "ranking")
    public Page<Subject> findRanking(Pageable page);

    @RestResource(path = "userIn", rel = "userIn")
    @Query("select count(s) > 0 from Subject s where s.id = :subject_id and ?#{ principal?.user } member of s.followers")
    public boolean existsByIdAndUsersIdIn(@Param("subject_id") Long subjectId);

}
