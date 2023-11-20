package com.sivan.crudapp.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "labels_posts")
public class LabelsPost {
    @EmbeddedId
    private LabelsPostId id;

}