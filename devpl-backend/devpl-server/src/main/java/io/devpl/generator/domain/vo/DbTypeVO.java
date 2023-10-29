package io.devpl.generator.domain.vo;

/**
 * 数据库类型VO
 */
public class DbTypeVO {

    private String id;
    private String name;
    private int defaultPort;

    public DbTypeVO(String id, String name, int defaultPort) {
        this.id = id;
        this.name = name;
        this.defaultPort = defaultPort;
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

    public int getDefaultPort() {
        return defaultPort;
    }

    public void setDefaultPort(int defaultPort) {
        this.defaultPort = defaultPort;
    }
}
