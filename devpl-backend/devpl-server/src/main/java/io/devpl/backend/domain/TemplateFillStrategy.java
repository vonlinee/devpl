package io.devpl.backend.domain;

public enum TemplateFillStrategy {

    /**
     * 数据库表，预定义了填充行为
     */
    DB_TABLE("db_table", "数据库表"),

    /**
     * 手动填充
     */
    MANUAL("manual", "手动填充");

    private final String id;
    private final String name;

    TemplateFillStrategy(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static TemplateFillStrategy find(String id) {
        for (TemplateFillStrategy item : values()) {
            if (item.getId().equalsIgnoreCase(id)) {
                return item;
            }
        }
        return null;
    }
}
