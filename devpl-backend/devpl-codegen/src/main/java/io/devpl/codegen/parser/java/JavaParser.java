package io.devpl.codegen.parser.java;

import java.io.InputStream;

public interface JavaParser {

    ClassParseResult parseClass(InputStream source);
}
