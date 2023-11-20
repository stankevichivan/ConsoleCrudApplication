package com.sivan.crudapp.repository;

import com.sivan.crudapp.model.Label;

import java.util.List;

public interface LabelRepository extends GenericRepository<Label, Long> {

    Label create(Label label);

    boolean addLabelToPost(Long postId, Long labelId);

    List<Label> getAllByPostId(Long postId);

    void deleteLabelFromPost(Long labelId, Long postId);
}
