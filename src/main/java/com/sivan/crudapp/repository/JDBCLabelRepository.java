package com.sivan.crudapp.repository;

import com.sivan.crudapp.model.Label;

import java.util.List;

public interface JDBCLabelRepository extends JDBCGenericRepository<Label, Long> {
    boolean addLabelToPost(Long postId, Long labelId);

    List<Label> getAllByPostId(Long postId);

    void deleteLabelFromPost(Long labelId);
}
