package com.sivan.crudapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Label {
    private Long id;
    private String name;

    @Override
    public String toString() {
        return "Label{" +
               "id=" + id +
               ", name='" + name + '}';
    }
}
