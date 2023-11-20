package com.sivan.crudapp.sevice;

import com.sivan.crudapp.model.Post;

public interface PostService extends Service<Post, Long> {
    Post addLabelToPost(Long postId, Long labelId);

    void deleteLabelFromPost(Long labelId, Long postId);
}
