package com.unicast.unicast_backend.persistance.repository.rest;

import java.util.List;
import java.util.Optional;

import com.unicast.unicast_backend.persistance.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends JpaRepositoryExportedFalse<User, Long> {

	@Override
	@RestResource(exported = true)
	Optional<User> findById(Long id);

	@PreAuthorize("hasAuthority('DELETE_USER_PRIVILEGE')")
	@Override
	@RestResource(exported = true)
	void deleteById(Long id);

	@PreAuthorize("hasAuthority('DELETE_USER_PRIVILEGE')")
	@Override
	@RestResource(exported = true)
	void delete(User subject);

	@RestResource(path = "byUsername", rel = "byUsername")
	public User findByUsername(@Param("username") String username);

	@Transactional
	@Modifying
	@RestResource(exported = false)
	default <S extends User> S saveInternal(final S entity) {
	  return save(entity);
	}

	@Query("select distinct s.followers from Subject s where ?1 member of s.professors")
	public List<User> findFollowersOfProfessorSubjects(User professor);

	@RestResource(path = "professors", rel = "professors")
	@Query("select distinct s.professors from Subject s join User u on u.id = ?#{ principal?.id } where u member of s.followers")
	public Page<User> findProfessors(Pageable page);

	@Query("select distinct s.professors from Subject s join User u on u.id = ?#{ principal?.id } where u member of s.followers")
	public List<User> findProfessors(User user);

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

	public List<User> findByIdNotIn(List<Long> userIds);

}
