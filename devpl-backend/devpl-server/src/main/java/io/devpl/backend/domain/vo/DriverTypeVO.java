package io.devpl.backend.domain.vo;

import io.devpl.codegen.db.JDBCDriver;
import lombok.Getter;
import lombok.Setter;

/**
 * 驱动类型VO
 */
@Getter
@Setter
public class DriverTypeVO {

    /**
     * id
     *
     * @see JDBCDriver#name()
     */
    private String id;

    /**
     * 驱动名称
     */
    private String name;

    /**
     * 该驱动对应的数据库的默认端口
     */
    private int defaultPort;

    public DriverTypeVO(String id, String name, int defaultPort) {
        this.id = id;
        this.name = name;
        this.defaultPort = defaultPort;
    }
}
