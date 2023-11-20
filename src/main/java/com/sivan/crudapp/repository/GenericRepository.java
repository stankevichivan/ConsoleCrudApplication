package com.sivan.crudapp.repository;

import com.sivan.crudapp.model.Label;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {
    T create(T t);

    Optional<T> getById(ID id);

    List<T> getAll();

    boolean update(T t);

    boolean deleteById(ID id);

    void deleteAll();
}
