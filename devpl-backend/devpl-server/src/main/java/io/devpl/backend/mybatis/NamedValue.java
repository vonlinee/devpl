package io.devpl.backend.mybatis;

public class NamedValue {

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private Object value;

    public NamedValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
