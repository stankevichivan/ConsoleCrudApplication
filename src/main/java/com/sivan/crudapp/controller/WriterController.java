package com.sivan.crudapp.controller;

import com.sivan.crudapp.model.Writer;
import com.sivan.crudapp.sevice.WriterService;
import com.sivan.crudapp.sevice.impl.WriterServiceImpl;

import java.util.List;
import java.util.Optional;

public class WriterController {

    private final WriterService writerService;
    //private final PostRepository postRepository;

    public WriterController() {
        this.writerService = new WriterServiceImpl();
        //this.postRepository = new PostRepositoryImpl();
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

//    public Writer addPostToWriter(long postId, long writerId) {
//        var post = postRepository.getById(postId);
//        var writer = repository.getById(writerId);
//        if (writer == null || post == null) {
//            return null;
//        }
//        if (writer.getPosts() == null) {
//            List<Post> posts = new ArrayList<>();
//            posts.add(post);
//            writer.setPosts(posts);
//            updateWriter(writer);
//            return writer;
//        }
//        writer.getPosts().add(post);
//        updateWriter(writer);
//        return writer;
//    }
//
//    public void deletePostFromWriter(long postId, long writerId) {
//        var writer = repository.getById(writerId);
//        writer.setPosts(writer.getPosts().stream().filter(post -> post.getId() != postId).toList());
//        updateWriter(writer);
//    }
}
