package io.devpl.fxui.controller.mbg;

public enum FileType {

    JAVA("java"),
    SQL("sql"),
    XML("xml");

    final String extension;

    FileType(String extension) {
        this.extension = extension;
    }
}
