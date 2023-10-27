package io.devpl.generator.domain.vo;

/**
 * 数据库类型VO
 */
public class DbTypeVO {

    private String id;
    private String name;

    public DbTypeVO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
