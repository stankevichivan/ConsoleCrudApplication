package com.sivan.crudapp.sevice;

import com.sivan.crudapp.model.Writer;

public interface WriterService extends Service<Writer, Long> {
    Writer addPostToWriter(Long writerId, Long postId);

    void deletePostFromWriter(Long postId);
}
