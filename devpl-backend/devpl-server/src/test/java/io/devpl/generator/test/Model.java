package io.devpl.generator.test;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Model<T> {

    private T item;

    private List<T> list;
}
