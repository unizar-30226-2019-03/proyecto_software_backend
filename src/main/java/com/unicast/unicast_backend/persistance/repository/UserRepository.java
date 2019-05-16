package com.unicast.unicast_backend.persistance.repository;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.User;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends JpaRepositoryExportedFalse<User, Long> {

    @PreAuthorize("hasAuthority('DELETE_USER_PRIVILEGE')")
    @Override
    void deleteById(Long id);

    @PreAuthorize("hasAuthority('DELETE_USER_PRIVILEGE')")
    @Override
    void delete(User subject);

    User findByUsername(String username);

    // Busquedas de usuarios que comienzan con un string dado

    @RestResource(path = "nameStartsWith", rel = "nameStartsWith")
    public List<User> findByNameStartsWithIgnoreCase(@Param("name") String name);

    @RestResource(path = "usernameStartsWith", rel = "usernameStartsWith")
    public List<User> findByUsernameStartsWithIgnoreCase(@Param("username") String username);

    @RestResource(path = "surnamesStartsWith", rel = "surnamesStartsWith")
    public List<User> findBySurnamesStartsWithIgnoreCase(@Param("surnames") String name);

    // Busqueda de usuarios que contiene un string dado

    @RestResource(path = "nameContaining", rel = "nameContaining")
    public List<User> findByNameContainingIgnoreCase(@Param("name") String name);

    @RestResource(path = "usernameContaining", rel = "usernameContaining")
    public List<User> findByUsernameContainingIgnoreCase(@Param("username") String username);

    @RestResource(path = "surnamesContaining", rel = "surnamesContaining")
    public List<User> findBySurnamesContainingIgnoreCase(@Param("surnames") String surnames);

}
