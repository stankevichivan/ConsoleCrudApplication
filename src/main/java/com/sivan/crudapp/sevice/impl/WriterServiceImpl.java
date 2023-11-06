package com.sivan.crudapp.sevice.impl;

import com.sivan.crudapp.model.Writer;
import com.sivan.crudapp.repository.JDBCPostRepository;
import com.sivan.crudapp.repository.JDBCWriterRepository;
import com.sivan.crudapp.repository.impl.JDBCPostRepositoryImpl;
import com.sivan.crudapp.repository.impl.JDBCWriterRepositoryImpl;
import com.sivan.crudapp.sevice.WriterService;

import java.util.List;
import java.util.Optional;

public class WriterServiceImpl implements WriterService {

    JDBCWriterRepository repository;
    JDBCPostRepository postRepository;

    public WriterServiceImpl() {
        this.repository = new JDBCWriterRepositoryImpl();
        this.postRepository = new JDBCPostRepositoryImpl();
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
        var writer = repository.getById(id);
        writer.ifPresent(value -> value.setPosts(postRepository.getAllByWriterId(value.getId())));
        return repository.getById(id);
    }

    @Override
    public List<Writer> getAll() {
        var writers = repository.getAll();
        writers.forEach(writer -> writer.setPosts(postRepository.getAllByWriterId(writer.getId())));
        return writers;
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
    public void deletePostFromWriter(Long postId) {
        postRepository.deletePostFromWriter(postId);
    }
}
