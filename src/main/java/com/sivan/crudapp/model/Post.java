package com.sivan.crudapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class Post {
    private long id;
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;
    private PostStatus postStatus;
    private List<Label> labels;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", postStatus=" + postStatus +
                ", labels=" + labels +
                '}';
    }
}
