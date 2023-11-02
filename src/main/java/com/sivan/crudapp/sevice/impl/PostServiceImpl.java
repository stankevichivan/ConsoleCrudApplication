package com.sivan.crudapp.sevice.impl;

import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.repository.JDBCPostRepository;
import com.sivan.crudapp.repository.impl.JDBCPostRepositoryImpl;
import com.sivan.crudapp.repository.impl.JDBCLabelRepositoryImpl;
import com.sivan.crudapp.sevice.PostService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PostServiceImpl implements PostService {

    JDBCPostRepository postRepository;

    public PostServiceImpl() {
        this.postRepository = new JDBCPostRepositoryImpl();
    }

    @Override
    public Post create(Post post) {
        return postRepository.create(post);
    }

    @Override
    public boolean delete(Long id) {
        return postRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        postRepository.deleteAll();
    }

    @Override
    public boolean update(Post post) {
        getById(post.getId()).ifPresent(val -> post.setCreated(val.getCreated()));
        return postRepository.update(post);
    }

    @Override
    public Optional<Post> getById(Long id) {
        var result = postRepository.getById(id);
        result.ifPresent(post -> post.setLabels(JDBCLabelRepositoryImpl.getInstance().getAllByPostId(post.getId())));
        return result;
    }

    @Override
    public List<Post> getAll() {
        var posts = postRepository.getAll();
        posts.forEach(post -> post.setLabels(JDBCLabelRepositoryImpl.getInstance().getAllByPostId(post.getId())));
        return posts;
    }

    @Override
    public Post addLabelToPost(Long postId, Long labelId) {
        var added = JDBCLabelRepositoryImpl.getInstance().addLabelToPost(postId, labelId);
        if (added) {
            var post = getById(postId).orElse(null);
            JDBCLabelRepositoryImpl.getInstance().getById(labelId).ifPresent(label -> {
                if (Objects.nonNull(post) && Objects.nonNull(post.getLabels())) {
                    post.getLabels().add(label);
                }
            });
            return post;
        }
        return null;
    }

    @Override
    public void deleteLabelFromPost(Long labelId) {
        JDBCLabelRepositoryImpl.getInstance().deleteLabelFromPost(labelId);
    }
}
