package com.sivan.crudapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Writer {
    private Long id;
    private String firstName;
    private String lastName;
    private Status status;
    private List<Post> posts;

    @Override
    public String toString() {
        return "Writer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status=" + status +
                ", posts=" + posts +
                '}';
    }
}
