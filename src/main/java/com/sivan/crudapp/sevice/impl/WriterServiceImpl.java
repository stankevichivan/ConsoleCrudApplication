package com.sivan.crudapp.sevice.impl;

import com.sivan.crudapp.model.Writer;
import com.sivan.crudapp.repository.JDBCWriterRepository;
import com.sivan.crudapp.repository.impl.JDBCWriterRepositoryImpl;
import com.sivan.crudapp.sevice.WriterService;

import java.util.List;
import java.util.Optional;

public class WriterServiceImpl implements WriterService {

    JDBCWriterRepository repository;

    public WriterServiceImpl() {
        this.repository = new JDBCWriterRepositoryImpl();
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
}
