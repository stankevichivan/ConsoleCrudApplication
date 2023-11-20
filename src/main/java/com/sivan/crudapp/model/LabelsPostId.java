package com.sivan.crudapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class LabelsPostId implements Serializable {
    private static final long serialVersionUID = 5884091379199895174L;
    @Column(name = "label_id", nullable = false)
    private Long labelId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LabelsPostId entity = (LabelsPostId) o;
        return Objects.equals(this.labelId, entity.labelId) &&
               Objects.equals(this.postId, entity.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labelId, postId);
    }

}