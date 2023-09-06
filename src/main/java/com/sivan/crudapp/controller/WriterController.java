package com.sivan.crudapp.controller;

import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.model.Writer;
import com.sivan.crudapp.repository.PostRepository;
import com.sivan.crudapp.repository.WriterRepository;
import com.sivan.crudapp.repository.impl.PostRepositoryImpl;
import com.sivan.crudapp.repository.impl.WriterRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WriterController {

    private final WriterRepository repository;
    private final PostRepository postRepository;

    public WriterController() {
        this.repository = new WriterRepositoryImpl();
        this.postRepository = new PostRepositoryImpl();
    }

    public Writer createWriter(Writer writer) {
        return repository.create(writer);
    }

    public Optional<Object> getWriterById(Long writerId) {
        return Optional.ofNullable(repository.getById(writerId));
    }

    public List<Writer> getAllWriters() {
        return repository.getAll();
    }

    public void updateWriter(Writer writer) {
        repository.update(writer);
    }

    public Optional<Object> deleteWriterById(long writerId) {
        return Optional.ofNullable(repository.deleteById(writerId));
    }

    public Writer addPostToWriter(long postId, long writerId) {
        var post = postRepository.getById(postId);
        var writer = repository.getById(writerId);
        if (writer == null || post == null) {
            return null;
        }
        if (writer.getPosts() == null) {
            List<Post> posts = new ArrayList<>();
            posts.add(post);
            writer.setPosts(posts);
            updateWriter(writer);
            return writer;
        }
        writer.getPosts().add(post);
        updateWriter(writer);
        return writer;
    }

    public void deletePostFromWriter(long postId, long writerId) {
        var writer = repository.getById(writerId);
        writer.setPosts(writer.getPosts().stream().filter(post -> post.getId() != postId).toList());
        updateWriter(writer);
    }
}
