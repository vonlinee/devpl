package io.devpl.backend.tools.parser.java;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MetaModel {

    private String name;

    private List<MetaField> fields;
}
