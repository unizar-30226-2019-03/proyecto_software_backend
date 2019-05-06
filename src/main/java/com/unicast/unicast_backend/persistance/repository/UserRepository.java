package com.unicast.unicast_backend.persistance.repository;

import java.util.List;

import com.unicast.unicast_backend.persistance.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    // Busquedas de usuarios que comienzan con un string dado

    @RestResource(path = "nameStartsWith", rel = "nameStartsWith")
    public List<User> findByNameStartsWith(@Param("name") String name);

    @RestResource(path = "usernameStartsWith", rel = "usernameStartsWith")
    public List<User> findByUsernameStartsWith(@Param("username") String username);

    @RestResource(path = "surnamesStartsWith", rel = "surnamesStartsWith")
    public List<User> findBySurnamesStartsWith(@Param("surnames") String name);

    // Busqueda de usuarios que contiene un string dado

    @RestResource(path = "nameContaining", rel = "nameContaining")
    public List<User> findByNameContaining(@Param("name") String name);

    @RestResource(path = "usernameContaining", rel = "usernameContaining")
    public List<User> findByUsernameContaining(@Param("username") String username);

    @RestResource(path = "surnamesContaining", rel = "surnamesContaining")
    public List<User> findBySurnamesContaining(@Param("surnames") String surnames);

}
