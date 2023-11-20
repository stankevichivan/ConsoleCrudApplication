package com.sivan.crudapp.controller;

import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.sevice.PostService;
import com.sivan.crudapp.sevice.impl.PostServiceImpl;

import java.util.List;
import java.util.Optional;

public class PostController {
    private final PostService service;

    public PostController() {
        service = new PostServiceImpl();
    }

    public Post createPost(Post post) {
        return service.create(post);
    }

    public List<Post> getAllPosts() {
        return service.getAll();
    }

    public Optional<Post> getPostById(Long postId) {
        return service.getById(postId);
    }

    public void updatePost(Post post) {
        service.update(post);

    }

    public boolean deletePostById(Long postId) {
        return service.delete(postId);
    }

    public Post addLabelToPost(Long postId, Long labelId) {
        return service.addLabelToPost(postId, labelId);
    }

    public void deleteLabelFromPost(Long labelId, Long postId) {
        service.deleteLabelFromPost(labelId, postId);
    }
}
