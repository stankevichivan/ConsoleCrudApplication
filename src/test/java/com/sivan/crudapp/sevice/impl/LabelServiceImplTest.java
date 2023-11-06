package com.sivan.crudapp.sevice.impl;

import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.repository.impl.JDBCLabelRepositoryImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LabelServiceImplTest {

    @Mock
    JDBCLabelRepositoryImpl jdbcLabelRepository;
    @InjectMocks
    LabelServiceImpl labelService = new LabelServiceImpl();
    Label label;
    private static MockedStatic<JDBCLabelRepositoryImpl> mockedStatic;

    @BeforeAll
    static void init() {
        mockedStatic = mockStatic(JDBCLabelRepositoryImpl.class);
    }

    @AfterAll
    static void afterAll() {
        mockedStatic.close();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.label = Label.builder().name("test").build();
    }

    @Test
    void create() {
        Mockito.when(JDBCLabelRepositoryImpl.getInstance()).thenReturn(jdbcLabelRepository);
        Mockito.when(jdbcLabelRepository.create(Mockito.any(Label.class))).thenReturn(label);
        var result = labelService.create(label);
        assertThat(result).isNotNull();
    }

    @Test
    void delete() {
        Mockito.when(JDBCLabelRepositoryImpl.getInstance()).thenReturn(jdbcLabelRepository);
        Mockito.when(jdbcLabelRepository.deleteById(anyLong())).thenReturn(true);
        var deleted = labelService.delete(1L);
        assertThat(deleted).isTrue();
    }

    @Test
    void update() {
        Mockito.when(JDBCLabelRepositoryImpl.getInstance()).thenReturn(jdbcLabelRepository);
        Mockito.when(jdbcLabelRepository.update(Mockito.any(Label.class))).thenReturn(true);
        var updated = labelService.update(label);
        assertThat(updated).isTrue();
    }

    @Test
    void getById() {
        Mockito.when(JDBCLabelRepositoryImpl.getInstance()).thenReturn(jdbcLabelRepository);
        Mockito.when(jdbcLabelRepository.getById(anyLong())).thenReturn(Optional.of(label));
        var label = labelService.getById(1L);
        label.ifPresent(value -> assertThat(value.getName()).isEqualTo("test"));
    }

    @Test
    void getAll() {
        Mockito.when(JDBCLabelRepositoryImpl.getInstance()).thenReturn(jdbcLabelRepository);
        Mockito.when(jdbcLabelRepository.getAll()).thenReturn(List.of(label, label));
        var labels = labelService.getAll();
        assertThat(labels).hasSize(2);
    }
}