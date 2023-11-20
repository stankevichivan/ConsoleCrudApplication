package com.sivan.crudapp.sevice.impl;

import com.sivan.crudapp.model.Writer;
import com.sivan.crudapp.repository.PostRepository;
import com.sivan.crudapp.repository.WriterRepository;
import com.sivan.crudapp.repository.hibernate.HibernatePostRepository;
import com.sivan.crudapp.repository.hibernate.HibernateWriterRepository;
import com.sivan.crudapp.sevice.WriterService;

import java.util.List;
import java.util.Optional;

public class WriterServiceImpl implements WriterService {

    WriterRepository repository;
    PostRepository postRepository;

    public WriterServiceImpl() {
        this.repository = new HibernateWriterRepository();
        this.postRepository = new HibernatePostRepository();
    }

    @Override
    public Writer create(Writer writer) {
        return repository.create(writer);
    }

    @Override
    public boolean delete(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public boolean update(Writer writer) {
        return repository.update(writer);
    }

    @Override
    public Optional<Writer> getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public List<Writer> getAll() {
        return repository.getAll();
    }

    @Override
    public Writer addPostToWriter(Long writerId, Long postId) {
        var added = postRepository.addPostToWriter(postId, writerId);
        if (added) {
            return getById(postId).orElse(null);
        }
        return null;
    }

    @Override
    public void deletePostFromWriter(Long postId, Long writerId) {
        postRepository.deletePostFromWriter(postId, writerId);
    }
}
