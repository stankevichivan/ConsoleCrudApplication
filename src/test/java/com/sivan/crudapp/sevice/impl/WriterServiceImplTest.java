package com.sivan.crudapp.sevice.impl;

import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.model.PostStatus;
import com.sivan.crudapp.model.Writer;
import com.sivan.crudapp.repository.PostRepository;
import com.sivan.crudapp.repository.WriterRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WriterServiceImplTest {
    @Mock
    WriterRepository writerRepository;
    @Mock
    PostRepository postRepository;

    @InjectMocks
    WriterServiceImpl service = new WriterServiceImpl();

    static Writer writer = new Writer();
    static Post post = new Post();
    static List<Post> posts = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        posts = List.of(post, post);

        writer.setId(1L);
        writer.setFirstName("ivan");
        writer.setLastName("stankevich");
        writer.setPosts(posts);

        post.setId(1L);
        post.setPostStatus(PostStatus.ACTIVE);
        post.setUpdated(LocalDateTime.now());
        post.setCreated(LocalDateTime.now().minusDays(2));
        post.setContent("content");

    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(postRepository, writerRepository);
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