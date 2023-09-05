package com.baomidou.mybatisplus.generator.type;

import java.sql.SQLType;

/**
 * Type Adapter
 * <p>
 * <a href="https://blog.csdn.net/qq_44868502/article/details/104846191">...</a>
 * https://blog.csdn.net/wei198621/article/details/113681660
 * </p>
 * SQLType（由各个厂商实现） <-> JDBCType（JDBC实现） <-> Standard Java Type (Java定义) <-> 自己的类型
 */
public interface TypeAdapter {

    boolean support(SQLType sqlType);

    /**
     * 从SQL类型转为Java类型
     * @param sqlType SQL类型
     * @return JavaType
     */
    JavaType from(SQLType sqlType);

    boolean support(JavaType javaType);

    SQLType from(JavaType javaType);
}
