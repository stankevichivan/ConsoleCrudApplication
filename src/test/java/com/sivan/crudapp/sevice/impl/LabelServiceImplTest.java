package com.sivan.crudapp.sevice.impl;

import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.repository.LabelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LabelServiceImplTest {

    @Mock
    LabelRepository labelRepository;
    @InjectMocks
    LabelServiceImpl labelService = new LabelServiceImpl();
    Label label = new Label();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        label.setId(1L);
        label.setName("test");
    }

    @Test
    void create() {
        Mockito.when(labelRepository.create(Mockito.any(Label.class))).thenReturn(label);
        var result = labelService.create(label);
        assertThat(result).isNotNull();
    }

    @Test
    void delete() {
        Mockito.when(labelRepository.deleteById(anyLong())).thenReturn(true);
        var deleted = labelService.delete(1L);
        assertThat(deleted).isTrue();
    }

    @Test
    void update() {
        Mockito.when(labelRepository.update(Mockito.any(Label.class))).thenReturn(true);
        var updated = labelService.update(label);
        assertThat(updated).isTrue();
    }

    @Test
    void getById() {
        Mockito.when(labelRepository.getById(anyLong())).thenReturn(Optional.of(label));
        var label = labelService.getById(1L);
        label.ifPresent(value -> assertThat(value.getName()).isEqualTo("test"));
    }

    @Test
    void getAll() {
        Mockito.when(labelRepository.getAll()).thenReturn(List.of(label, label));
        var labels = labelService.getAll();
        assertThat(labels).hasSize(2);
    }
}