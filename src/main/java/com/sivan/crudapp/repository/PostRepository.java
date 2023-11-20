package com.sivan.crudapp.repository;

import com.sivan.crudapp.model.Post;

import java.util.List;

public interface PostRepository extends GenericRepository<Post, Long> {
    boolean addPostToWriter(Long postId, Long writerId);

    List<Post> getAllByWriterId(Long writerId);

    void deletePostFromWriter(Long postId, Long writerId);
}
