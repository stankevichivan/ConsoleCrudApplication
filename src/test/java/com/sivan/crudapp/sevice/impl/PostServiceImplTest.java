package com.sivan.crudapp.sevice.impl;

import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.model.PostStatus;
import com.sivan.crudapp.repository.JDBCPostRepository;
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
class PostServiceImplTest {

    @Mock
    JDBCPostRepository postRepository;
    @Mock
    JDBCLabelRepositoryImpl labelRepository;

    @InjectMocks
    PostServiceImpl service = new PostServiceImpl();

    static Post post;
    static List<Label> labels;
    static Label label;

    private static MockedStatic<JDBCLabelRepositoryImpl> mockedStatic;

    @BeforeAll
    static void beforeAll() {
        mockedStatic = mockStatic(JDBCLabelRepositoryImpl.class);
        label = Label.builder().id(1L).name("test").build();
        labels = List.of(label, Label.builder().id(2L).name("test2").build());
        post = Post.builder()
                .id(1L)
                .postStatus(PostStatus.ACTIVE)
                .updated(LocalDateTime.now())
                .content("content")
                .created(LocalDateTime.now().minusDays(2))
                .labels(labels)
                .build();
    }

    @AfterAll
    static void afterAll() {
        mockedStatic.close();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Mockito.reset(postRepository, labelRepository, JDBCLabelRepositoryImpl.class);
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
        Mockito.when(JDBCLabelRepositoryImpl.getInstance()).thenReturn(labelRepository);
        Mockito.when(labelRepository.getAllByPostId(anyLong())).thenReturn(labels);
        Mockito.when(postRepository.getById(anyLong())).thenReturn(Optional.of(post));
        var post = service.getById(1L);
        post.ifPresent(value -> assertThat(value.toString()).isEqualTo(PostServiceImplTest.post.toString()));
    }

    @Test
    void getAll() {
        Mockito.when(JDBCLabelRepositoryImpl.getInstance()).thenReturn(labelRepository);
        Mockito.when(labelRepository.getAllByPostId(anyLong())).thenReturn(labels);
        Mockito.when(postRepository.getAll()).thenReturn(List.of(post, post));
        var posts = service.getAll();
        assertThat(posts).hasSize(2);
    }

    @Test
    void addLabelToPost() {
        Mockito.when(JDBCLabelRepositoryImpl.getInstance()).thenReturn(labelRepository);
        Mockito.when(labelRepository.addLabelToPost(anyLong(), anyLong())).thenReturn(true);
        Mockito.when(labelRepository.getById(anyLong())).thenReturn(Optional.of(label));
        Mockito.when(postRepository.getById(anyLong())).thenReturn(Optional.of(PostServiceImplTest.post));
        var post = service.addLabelToPost(1L, 1L);
        assertThat(post.getLabels()).isNotEmpty();
    }

    @Test
    void addLabelToPostWithNull() {
        Mockito.when(JDBCLabelRepositoryImpl.getInstance()).thenReturn(labelRepository);
        Mockito.when(labelRepository.addLabelToPost(anyLong(), anyLong())).thenReturn(false);
        var post = service.addLabelToPost(1L, 1L);
        assertThat(post).isNull();
    }
}