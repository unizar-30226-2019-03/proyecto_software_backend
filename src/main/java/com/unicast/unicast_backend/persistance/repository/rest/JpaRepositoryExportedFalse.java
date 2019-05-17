package com.unicast.unicast_backend.persistance.repository.rest;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.rest.core.annotation.RestResource;

@NoRepositoryBean
public interface JpaRepositoryExportedFalse<T, ID> extends JpaRepository<T, ID> {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findAll()
	 */
    @Override
    @RestResource(exported = false)
	List<T> findAll();

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.PagingAndSortingRepository#findAll(org.springframework.data.domain.Sort)
	 */
    @Override
    @RestResource(exported = false)
	List<T> findAll(Sort sort);

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#findAll(java.lang.Iterable)
	 */
    @Override
    @RestResource(exported = false)
	List<T> findAllById(Iterable<ID> ids);

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.CrudRepository#save(java.lang.Iterable)
	 */
    @Override
    @RestResource(exported = false)
	<S extends T> List<S> saveAll(Iterable<S> entities);

	/**
	 * Flushes all pending changes to the database.
	 */
    @Override
    @RestResource(exported = false)
	void flush();

	/**
	 * Saves an entity and flushes changes instantly.
	 *
	 * @param entity
	 * @return the saved entity
	 */
    @Override
    @RestResource(exported = false)
	<S extends T> S saveAndFlush(S entity);

	/**
	 * Deletes the given entities in a batch which means it will create a single {@link Query}. Assume that we will clear
	 * the {@link javax.persistence.EntityManager} after the call.
	 *
	 * @param entities
	 */
    @Override
    @RestResource(exported = false)
	void deleteInBatch(Iterable<T> entities);

	/**
	 * Deletes all entities in a batch call.
	 */
    @Override
    @RestResource(exported = false)
	void deleteAllInBatch();

	/**
	 * Returns a reference to the entity with the given identifier. Depending on how the JPA persistence provider is
	 * implemented this is very likely to always return an instance and throw an
	 * {@link javax.persistence.EntityNotFoundException} on first access. Some of them will reject invalid identifiers
	 * immediately.
	 *
	 * @param id must not be {@literal null}.
	 * @return a reference to the entity with the given identifier.
	 * @see EntityManager#getReference(Class, Object) for details on when an exception is thrown.
	 */
    @Override
    @RestResource(exported = false)
	T getOne(ID id);

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.query.QueryByExampleExecutor#findAll(org.springframework.data.domain.Example)
	 */
	@Override
    @RestResource(exported = false)
	<S extends T> List<S> findAll(Example<S> example);

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.query.QueryByExampleExecutor#findAll(org.springframework.data.domain.Example, org.springframework.data.domain.Sort)
	 */
	@Override
    @RestResource(exported = false)
    <S extends T> List<S> findAll(Example<S> example, Sort sort);
    

	/**
	 * Returns a single entity matching the given {@link Example} or {@literal null} if none was found.
	 *
	 * @param example must not be {@literal null}.
	 * @return a single entity matching the given {@link Example} or {@link Optional#empty()} if none was found.
	 * @throws org.springframework.dao.IncorrectResultSizeDataAccessException if the Example yields more than one result.
	 */
	@Override
    @RestResource(exported = false)
	<S extends T> Optional<S> findOne(Example<S> example);

	/**
	 * Returns a {@link Page} of entities matching the given {@link Example}. In case no match could be found, an empty
	 * {@link Page} is returned.
	 *
	 * @param example must not be {@literal null}.
	 * @param pageable can be {@literal null}.
	 * @return a {@link Page} of entities matching the given {@link Example}.
	 */
	@Override
    @RestResource(exported = false)
	<S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

	/**
	 * Returns the number of instances matching the given {@link Example}.
	 *
	 * @param example the {@link Example} to count instances for. Must not be {@literal null}.
	 * @return the number of instances matching the {@link Example}.
	 */
	@Override
    @RestResource(exported = false)
	<S extends T> long count(Example<S> example);

	/**
	 * Checks whether the data store contains elements that match the given {@link Example}.
	 *
	 * @param example the {@link Example} to use for the existence check. Must not be {@literal null}.
	 * @return {@literal true} if the data store contains elements that match the given {@link Example}.
	 */
	@Override
    @RestResource(exported = false)
    <S extends T> boolean exists(Example<S> example);
    
	/**
	 * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
	 *
	 * @param pageable
	 * @return a page of entities
	 */
	@Override
    @RestResource(exported = false)
    Page<T> findAll(Pageable pageable);
    
    	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 *
	 * @param entity must not be {@literal null}.
	 * @return the saved entity will never be {@literal null}.
	 */
	@Override
    @RestResource(exported = false)
	<S extends T> S save(S entity);

	/**
	 * Retrieves an entity by its id.
	 *
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@literal Optional#empty()} if none found
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	@Override
    @RestResource(exported = false)
	Optional<T> findById(ID id);

	/**
	 * Returns whether an entity with the given id exists.
	 *
	 * @param id must not be {@literal null}.
	 * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	@Override
    @RestResource(exported = false)
	boolean existsById(ID id);

	/**
	 * Returns the number of entities available.
	 *
	 * @return the number of entities
	 */
	@Override
    @RestResource(exported = false)
	long count();

	/**
	 * Deletes the entity with the given id.
	 *
	 * @param id must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
	 */
	@Override
    @RestResource(exported = false)
	void deleteById(ID id);

	/**
	 * Deletes a given entity.
	 *
	 * @param entity
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	@Override
    @RestResource(exported = false)
	void delete(T entity);

	/**
	 * Deletes the given entities.
	 *
	 * @param entities
	 * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
	 */
	@Override
    @RestResource(exported = false)
	void deleteAll(Iterable<? extends T> entities);

	/**
	 * Deletes all entities managed by the repository.
	 */
	@Override
    @RestResource(exported = false)
	void deleteAll();
}