package com.baomidou.mybatisplus.generator.config.po;

/**
 * 表名拼接
 *
 * @author nieqiuqiu
 * @since 3.3.0
 */
public class LikeTable {

    private final String value;

    private SqlLike like = SqlLike.DEFAULT;

    public LikeTable(String value) {
        this.value = value;
    }

    public LikeTable(String value, SqlLike like) {
        this.value = value;
        this.like = like;
    }

    @Override
    public String toString() {
        return getValue();
    }

    /**
     * 用%连接like
     *
     * @return like 的值
     */
    public String getValue() {
        return switch (like) {
            case LEFT -> "%" + value;
            case RIGHT -> value + "%";
            default -> "%" + value + "%";
        };
    }
}
