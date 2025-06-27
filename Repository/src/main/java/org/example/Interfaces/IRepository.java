package org.example.Interfaces;


import org.example.Entity;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, E extends Entity<T>> {

    /**
     *
     * @param id - the id of the entity to be returned
     *                 id must not be null
     * @return the entity with the specified id
     *         or null if there is no entity with the given id
     * @throws IllegalArgumentException - if id is null
     */
    Optional<E> findOne(T id);

    /**
     * @return all entities
     */
    List<E> findAll();

    /**
     * saves the given entity in repository
     * @param entity - entity must not be null
     * @return null - if the given entity is saved
     *                otherwise returns the entity (id already exists)
     * @throws IllegalArgumentException - if the given entity is null
     */
    Optional<E> save(E entity);

    /**
     * removes the entity with the specified id
     * @param id - id must not be null
     * @return the removed entity or null if there is no entity with the given id
     * @throws IllegalArgumentException - if the given id is null
     */
    Optional<E> delete(T id);

    /**
     *
     * @param entity - entity must not be null
     * @return null - if the entity is updated
     *                otherwise returns the entity - (e.g. id does not exist)
     * @throws IllegalArgumentException - if the given entity is null
     */
    Optional<E> update(E entity);
}