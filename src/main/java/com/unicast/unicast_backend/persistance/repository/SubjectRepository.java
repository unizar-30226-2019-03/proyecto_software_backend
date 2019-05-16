package com.unicast.unicast_backend.persistance.repository;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.Subject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource(collectionResourceRel = "subjects", path = "subjects")
public interface SubjectRepository extends JpaRepositoryExportedFalse<Subject, Long> {

    @PreAuthorize("hasAuthority('DELETE_SUBJECT_PRIVILEGE')")
    @Override
    void deleteById(Long id);

    @PreAuthorize("hasAuthority('DELETE_SUBJECT_PRIVILEGE')")
    @Override
    void delete(Subject subject);

    @PreAuthorize("hasAnyAuthority('CREATE_SUBJECT_PRIVILEGE, UPDATE_SUBJECT_PRIVILEGE')")
    @Override
    <S extends Subject> S save(S entity);

    @RestResource(path = "nameStartsWith", rel = "nameStartsWith")
    public List<Subject> findByNameStartsWithIgnoreCase(@Param("name") String name);

    @RestResource(path = "nameContaining", rel = "nameContaining")
    public List<Subject> findByNameContainingIgnoreCase(@Param("name") String name);

    @RestResource(path = "name", rel = "name")
    public Subject findByNameIgnoreCase(@Param("name") String name);

    @RestResource(path = "ranking", rel = "ranking")
    public Page<Subject> findRanking(Pageable page);

    @RestResource(path = "userIn", rel = "userIn")
    @Query("select count(s) > 0 from Subject s where s.id = :subject_id and ?#{ principal?.user } member of s.followers")
    public boolean existsByIdAndUsersIdIn(@Param("subject_id") Long subjectId);

}
