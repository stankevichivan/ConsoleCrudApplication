package com.sivan.crudapp.sevice.impl;

import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.repository.impl.JDBCLabelRepositoryImpl;
import com.sivan.crudapp.sevice.LabelService;

import java.util.List;
import java.util.Optional;

public class LabelServiceImpl implements LabelService {
    @Override
    public Label create(Label label) {
        return JDBCLabelRepositoryImpl.getInstance().create(label);
    }

    @Override
    public boolean delete(Long id) {
        return JDBCLabelRepositoryImpl.getInstance().deleteById(id);
    }

    @Override
    public void deleteAll() {
        JDBCLabelRepositoryImpl.getInstance().deleteAll();
    }

    @Override
    public boolean update(Label label) {
        return JDBCLabelRepositoryImpl.getInstance().update(label);
    }

    @Override
    public Optional<Label> getById(Long id) {
        return JDBCLabelRepositoryImpl.getInstance().getById(id);
    }

    @Override
    public List<Label> getAll() {
        return JDBCLabelRepositoryImpl.getInstance().getAll();
    }
}
