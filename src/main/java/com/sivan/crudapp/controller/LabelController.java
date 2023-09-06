package com.sivan.crudapp.controller;

import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.repository.LabelRepository;
import com.sivan.crudapp.repository.impl.LabelRepositoryImpl;

import java.util.List;
import java.util.Optional;

public class LabelController {

    private final LabelRepository repository;

    public LabelController() {
        repository = new LabelRepositoryImpl();
    }

    public void deleteAllLabels() {
        repository.deleteAll();
    }

    public Optional<Label> deleteLabelById(Long labelId) {
        return Optional.ofNullable(repository.deleteById(labelId));
    }

    public void updateLabel(Label label) {
        repository.update(label);
    }

    public Optional<Label> getLabelById(Long labelId) {
        return Optional.ofNullable(repository.getById(labelId));
    }

    public List<Label> getAllLabels() {
        return repository.getAll();
    }

    public Label createLabel(Label label) {
        return repository.create(label);
    }
}
