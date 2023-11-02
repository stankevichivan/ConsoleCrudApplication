package com.sivan.crudapp.controller;

import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.sevice.LabelService;
import com.sivan.crudapp.sevice.impl.LabelServiceImpl;

import java.util.List;
import java.util.Optional;

public class LabelController {

    private final LabelService service;

    public LabelController() {
        this.service = new LabelServiceImpl();
    }

    public void deleteAllLabels() {
        service.deleteAll();
    }

    public boolean deleteLabelById(Long labelId) {
        return service.delete(labelId);
    }

    public boolean updateLabel(Label label) {
        return service.update(label);
    }

    public Optional<Label> getLabelById(Long labelId) {
        return service.getById(labelId);
    }

    public List<Label> getAllLabels() {
        return service.getAll();
    }

    public Label createLabel(Label label) {
        return service.create(label);
    }
}
