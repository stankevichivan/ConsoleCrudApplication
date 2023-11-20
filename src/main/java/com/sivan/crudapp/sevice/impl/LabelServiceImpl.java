package com.sivan.crudapp.sevice.impl;

import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.repository.LabelRepository;
import com.sivan.crudapp.repository.hibernate.HibernateLabelRepository;
import com.sivan.crudapp.sevice.LabelService;

import java.util.List;
import java.util.Optional;

public class LabelServiceImpl implements LabelService {

    LabelRepository labelRepository;

    public LabelServiceImpl() {
        this.labelRepository = new HibernateLabelRepository();
    }

    @Override
    public Label create(Label label) {
        return labelRepository.create(label);
    }

    @Override
    public boolean delete(Long id) {
        return labelRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        labelRepository.deleteAll();
    }

    @Override
    public boolean update(Label label) {
        return labelRepository.update(label);
    }

    @Override
    public Optional<Label> getById(Long id) {
        return labelRepository.getById(id);
    }

    @Override
    public List<Label> getAll() {
        return labelRepository.getAll();
    }
}
