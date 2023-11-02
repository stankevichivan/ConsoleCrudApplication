package com.sivan.crudapp.repository;

import java.util.List;
import java.util.Optional;

public interface JDBCGenericRepository<T, ID> {
    T create(T t);

    Optional<T> getById(ID id);

    List<T> getAll();

    boolean update(T t);

    boolean deleteById(ID id);

    void deleteAll();
}
