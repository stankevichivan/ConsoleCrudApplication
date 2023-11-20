package com.sivan.crudapp.sevice.impl;

import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.model.PostStatus;
import com.sivan.crudapp.repository.LabelRepository;
import com.sivan.crudapp.repository.PostRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostServiceImplTest {

    @Mock
    PostRepository postRepository;
    @Mock
    LabelRepository labelRepository;

    @InjectMocks
    PostServiceImpl service = new PostServiceImpl();

    static Post post = new Post();
    static List<Label> labels;
    static Label label = new Label();


    @BeforeAll
    static void beforeAll() {
        label.setId(1L);
        label.setName("test");

        var label2 = new Label();
        label.setId(2L);
        label.setName("test2");
        labels = List.of(label, label2);

        post.setId(1L);
        post.setPostStatus(PostStatus.ACTIVE);
        post.setUpdated(LocalDateTime.now());
        post.setCreated(LocalDateTime.now().minusDays(2));
        post.setContent("content");
        post.setLabels(labels);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(postRepository, labelRepository);
    }

    @Test
    void create() {
        Mockito.when(postRepository.create(any(Post.class))).thenReturn(post);
        var created = service.create(post);
        assertThat(created.toString()).isEqualTo(post.toString());
    }

    @Test
    void delete() {
        Mockito.when(postRepository.deleteById(anyLong())).thenReturn(true);
        var deleted = service.delete(1L);
        assertThat(deleted).isTrue();
    }

    @Test
    void update() {
        Mockito.when(postRepository.update(any(Post.class))).thenReturn(true);
        var updated = service.update(post);
        assertThat(updated).isTrue();
    }

    @Test
    void getById() {
        Mockito.when(labelRepository.getAllByPostId(anyLong())).thenReturn(labels);
        Mockito.when(postRepository.getById(anyLong())).thenReturn(Optional.of(post));
        var post = service.getById(1L);
        post.ifPresent(value -> assertThat(value.toString()).isEqualTo(PostServiceImplTest.post.toString()));
    }

    @Test
    void getAll() {
        Mockito.when(labelRepository.getAllByPostId(anyLong())).thenReturn(labels);
        Mockito.when(postRepository.getAll()).thenReturn(List.of(post, post));
        var posts = service.getAll();
        assertThat(posts).hasSize(2);
    }

    @Test
    void addLabelToPost() {
        Mockito.when(labelRepository.addLabelToPost(anyLong(), anyLong())).thenReturn(true);
        Mockito.when(labelRepository.getById(anyLong())).thenReturn(Optional.of(label));
        Mockito.when(labelRepository.getAllByPostId(anyLong())).thenReturn(labels);
        Mockito.when(postRepository.getById(anyLong())).thenReturn(Optional.of(PostServiceImplTest.post));
        var post = service.addLabelToPost(1L, 1L);
        assertThat(post.getLabels()).isNotEmpty();
    }

    @Test
    void addLabelToPostWithNull() {
        Mockito.when(labelRepository.addLabelToPost(anyLong(), anyLong())).thenReturn(false);
        var post = service.addLabelToPost(1L, 1L);
        assertThat(post).isNull();
    }
}