package com.sivan.crudapp.repository.impl;

import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.model.Status;
import com.sivan.crudapp.repository.LabelRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LabelRepositoryImplTest {
    LabelRepository labelRepository = new LabelRepositoryImpl();

    @Test
    void create() {
        var label1 = labelRepository.create(Label.builder().name("label2").status(Status.ACTIVE).build());
        assertNotNull(label1);
    }

    @Test
    void getAll() {
        var all = labelRepository.getAll();
        assertEquals(5, all.size());
    }

    @Test
    void getById() {
        var byId = labelRepository.getById(1L);
        assertEquals(1, byId.getId());

    }

    @Test
    void deleteById() {
        var result = labelRepository.deleteById(1L);
        assertEquals(Status.DELETED, result.getStatus());
    }

}