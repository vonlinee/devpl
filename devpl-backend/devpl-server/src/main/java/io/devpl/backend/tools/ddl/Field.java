package io.devpl.backend.tools.ddl;

import com.google.common.base.CaseFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class Field {

    public String name;

    private String type;

    private boolean primaryKey;

    private String comment;

    public Field() {
    }

    public Field(String name, String type) {
        this.name = name;
        this.type = type;
        this.primaryKey = false;
    }

    public Field(String name, String type, Boolean primaryKey, String comment) {
        this.name = name;
        this.type = type;
        this.primaryKey = primaryKey;
        this.comment = comment;
    }

    public static Field newField(String name, String type, boolean primaryKey, String comment) {
        return new Field(name, type, primaryKey, comment);
    }

    public static Field newField(String name, String type) {
        return new Field(name, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(name, field.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getTableColumn() {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.name);
    }

}
