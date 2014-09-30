package com.bitbucket.pelenthium.accountservice.repository;

/**
 * Created by cementovoz on 19.08.14.
 */
public interface Repository<T> {
    /**
     * Loaded object with id, or return null, if object not represent in database.
     *
     * @param id
     * @return
     */
    T getById(Integer id);

    /**
     * Save object
     *
     * @param object
     */
    void save(T object);
}
