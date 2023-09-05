package io.devpl.generator.test;

import lombok.Data;

import java.util.List;

@Data
public class Model<T> {

    private T item;

    private List<T> list;
}
