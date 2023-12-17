package io.devpl.codegen.db;

import lombok.Getter;

/**
 * 生成ID类型枚举类
 */
@Getter
public enum IdType {
    /**
     * 数据库ID自增
     * <p>该类型请确保数据库设置了 ID自增 否则无效</p>
     */
    AUTO(0),
    /**
     * 该类型为未设置主键类型(注解里等于跟随全局,全局里约等于 INPUT)
     */
    NONE(1),
    /**
     * 用户输入ID
     * <p>该类型可以通过自己注册自动填充插件进行填充</p>
     */
    INPUT(2),
    /**
     * 分配ID (主键类型为number或string）
     */
    ASSIGN_ID(3),
    /**
     * 分配UUID (主键类型为 string)
     */
    ASSIGN_UUID(4),
    /**
     * 分配ULID (主键类型为 string)
     */
    ASSIGN_ULID(5);

    private final int key;

    IdType(int key) {
        this.key = key;
    }
}
