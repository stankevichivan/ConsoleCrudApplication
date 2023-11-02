package com.sivan.crudapp.sevice.impl;

import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.repository.impl.LabelJDBCRepositoryImpl;
import com.sivan.crudapp.sevice.LabelService;

import java.util.List;
import java.util.Optional;

public class LabelServiceImpl implements LabelService {
    @Override
    public Label create(Label label) {
        return LabelJDBCRepositoryImpl.getInstance().create(label);
    }

    @Override
    public boolean delete(Long id) {
        return LabelJDBCRepositoryImpl.getInstance().deleteById(id);
    }

    @Override
    public void deleteAll() {
        LabelJDBCRepositoryImpl.getInstance().deleteAll();
    }

    @Override
    public boolean update(Label label) {
        return LabelJDBCRepositoryImpl.getInstance().update(label);
    }

    @Override
    public Optional<Label> getById(Long id) {
        return LabelJDBCRepositoryImpl.getInstance().getById(id);
    }

    @Override
    public List<Label> getAll() {
        return LabelJDBCRepositoryImpl.getInstance().getAll();
    }
}
