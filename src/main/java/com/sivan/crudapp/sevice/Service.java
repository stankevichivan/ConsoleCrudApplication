package com.sivan.crudapp.sevice;

import com.sivan.crudapp.model.Label;

import java.util.List;
import java.util.Optional;

public interface Service<T, ID> {
    T create(T t);

    boolean delete(ID id);

    void deleteAll();

    boolean update(T t);

    Optional<T> getById(ID id);

    List<T> getAll();
}
