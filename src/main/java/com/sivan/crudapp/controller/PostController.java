package com.sivan.crudapp.controller;

import com.sivan.crudapp.model.Label;
import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.repository.LabelRepository;
import com.sivan.crudapp.repository.PostRepository;
import com.sivan.crudapp.repository.impl.LabelRepositoryImpl;
import com.sivan.crudapp.repository.impl.PostRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostController {
    private final PostRepository repository;
    private final LabelRepository labelRepository;

    public PostController() {
        repository = new PostRepositoryImpl();
        labelRepository = new LabelRepositoryImpl();
    }

    public Post createPost(Post post) {
        return repository.create(post);
    }

    public List<Post> getAllPosts() {
        return repository.getAll();
    }

    public Optional<Post> getPostById(Long postId) {
        return Optional.ofNullable(repository.getById(postId));
    }

    public void updatePost(Post post) {
        repository.update(post);

    }

    public Optional<Object> deletePostById(Long postId) {
        return Optional.ofNullable(repository.deleteById(postId));
    }

    public Post addLabelToPost(Long postId, Long labelId) {
        var label = labelRepository.getById(labelId);
        var post = repository.getById(postId);
        if (label == null || post == null) {
            return null;
        }
        if (post.getLabels() == null) {
            List<Label> labels = new ArrayList<>();
            labels.add(label);
            post.setLabels(labels);
            updatePost(post);
            return post;
        }
        post.getLabels().add(label);
        updatePost(post);
        return post;
    }

    public void deleteLabelFromPost(Long postId, Long labelId) {
        var post = repository.getById(postId);
        post.setLabels(post.getLabels().stream().filter(label -> label.getId() != labelId).toList());
        updatePost(post);
    }
}
