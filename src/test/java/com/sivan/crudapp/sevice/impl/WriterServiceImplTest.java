package com.sivan.crudapp.sevice.impl;

import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.model.PostStatus;
import com.sivan.crudapp.model.Writer;
import com.sivan.crudapp.repository.JDBCPostRepository;
import com.sivan.crudapp.repository.JDBCWriterRepository;
import com.sivan.crudapp.repository.impl.JDBCLabelRepositoryImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WriterServiceImplTest {
    @Mock
    JDBCWriterRepository writerRepository;
    @Mock
    JDBCPostRepository postRepository;

    @InjectMocks
    WriterServiceImpl service = new WriterServiceImpl();

    static Writer writer;
    static Post post;
    static List<Post> posts;
    private static MockedStatic<JDBCLabelRepositoryImpl> mockedStatic;

    @BeforeAll
    static void beforeAll() {
        mockedStatic = mockStatic(JDBCLabelRepositoryImpl.class);
        writer = Writer.builder()
                .id(1L)
                .firstName("ivan")
                .lastName("stankevich")
                .build();
        post = Post.builder()
                .id(1L)
                .postStatus(PostStatus.ACTIVE)
                .updated(LocalDateTime.now())
                .content("content")
                .created(LocalDateTime.now().minusDays(2))
                .build();
        posts = List.of(post, post);
    }

    @AfterAll
    static void afterAll() {
        mockedStatic.close();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(postRepository, writerRepository, JDBCLabelRepositoryImpl.class);
    }


    @Test
    void create() {
        Mockito.when(writerRepository.create(any(Writer.class))).thenReturn(writer);
        var created = service.create(writer);
        assertThat(created.toString()).isEqualTo(writer.toString());

    }

    @Test
    void delete() {
        Mockito.when(writerRepository.deleteById(anyLong())).thenReturn(true);
        var deleted = service.delete(1L);
        assertThat(deleted).isTrue();
    }

    @Test
    void update() {
        Mockito.when(writerRepository.update(any(Writer.class))).thenReturn(true);
        var updated = service.update(writer);
        assertThat(updated).isTrue();
    }

    @Test
    void getById() {
        Mockito.when(postRepository.getAllByWriterId(anyLong())).thenReturn(posts);
        Mockito.when(writerRepository.getById(anyLong())).thenReturn(Optional.of(writer));
        var writer = service.getById(1L);
        writer.ifPresent(value -> assertThat(value.getPosts()).hasSize(2));
    }

    @Test
    void getAll() {
        Mockito.when(postRepository.getAllByWriterId(anyLong())).thenReturn(posts);
        Mockito.when(writerRepository.getAll()).thenReturn(List.of(writer, writer));
        var writers = service.getAll();
        assertThat(writers).hasSize(2);
    }

    @Test
    void addPostToWriter() {
        Mockito.when(postRepository.addPostToWriter(anyLong(), anyLong())).thenReturn(true);
        Mockito.when(writerRepository.getById(anyLong())).thenReturn(Optional.of(writer));
        var writer = service.addPostToWriter(1L, 1L);
        assertThat(writer.toString()).isEqualTo(WriterServiceImplTest.writer.toString());
    }

    @Test
    void addPostToWriterWithNull() {
        Mockito.when(postRepository.addPostToWriter(anyLong(), anyLong())).thenReturn(false);
        var writer = service.addPostToWriter(1L, 1L);
        assertThat(writer).isNull();
    }
}