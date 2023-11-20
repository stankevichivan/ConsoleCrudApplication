package com.sivan.crudapp.sevice.impl;

import com.sivan.crudapp.model.Post;
import com.sivan.crudapp.repository.LabelRepository;
import com.sivan.crudapp.repository.PostRepository;
import com.sivan.crudapp.repository.hibernate.HibernateLabelRepository;
import com.sivan.crudapp.repository.hibernate.HibernatePostRepository;
import com.sivan.crudapp.repository.impl.JDBCLabelRepositoryImpl;
import com.sivan.crudapp.sevice.PostService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    LabelRepository labelRepository;

    public PostServiceImpl() {
        this.postRepository = new HibernatePostRepository();
        this.labelRepository = new HibernateLabelRepository();
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
        result.ifPresent(post -> post.setLabels(labelRepository.getAllByPostId(post.getId())));
        return result;
    }

    @Override
    public List<Post> getAll() {
        var posts = postRepository.getAll();
        posts.forEach(post -> post.setLabels(labelRepository.getAllByPostId(post.getId())));
        return posts;
    }

    @Override
    public Post addLabelToPost(Long postId, Long labelId) {
        var added = labelRepository.addLabelToPost(postId, labelId);
        if (added) {
            return getById(postId).orElse(null);
        }
        return null;
    }

    @Override
    public void deleteLabelFromPost(Long labelId, Long postId) {
        labelRepository.deleteLabelFromPost(labelId, postId);
    }
}
