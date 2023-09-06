package com.sivan.crudapp.repository;

import java.util.List;

public interface GenericRepository<T, ID> {
    T create(T t);

    T getById(ID id);

    List<T> getAll();

    void update(T t);

    T deleteById(ID id);

    void deleteAll();
}
