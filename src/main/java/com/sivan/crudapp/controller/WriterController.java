package com.sivan.crudapp.controller;

import com.sivan.crudapp.model.Writer;
import com.sivan.crudapp.sevice.WriterService;
import com.sivan.crudapp.sevice.impl.WriterServiceImpl;

import java.util.List;
import java.util.Optional;

public class WriterController {

    private final WriterService writerService;

    public WriterController() {
        this.writerService = new WriterServiceImpl();
    }

    public Writer createWriter(Writer writer) {
        return writerService.create(writer);
    }

    public Optional<Object> getWriterById(Long writerId) {
        return Optional.ofNullable(writerService.getById(writerId));
    }

    public List<Writer> getAllWriters() {
        return writerService.getAll();
    }

    public void updateWriter(Writer writer) {
        writerService.update(writer);
    }

    public boolean deleteWriterById(long writerId) {
        return writerService.delete(writerId);
    }

    public Writer addPostToWriter(long postId, long writerId) {
        return writerService.addPostToWriter(writerId, postId);
    }

    public void deletePostFromWriter(long postId, long writerId) {
        writerService.deletePostFromWriter(postId, writerId);
    }
}
