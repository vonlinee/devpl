package io.devpl.backend.system;

import lombok.Data;

import java.util.Set;

/**
 * 路由元数据
 */
@Data
public class RouteMeta {

    /**
     * 图标
     */
    private String icon;

    /**
     * 标题
     */
    private String title;

    /**
     * 角色列表
     */
    private Set<String> roles;

    /**
     * 等级，用于排序
     */
    private Integer rank;

    /**
     * 跳转地址
     */
    private String frameSrc;

    /**
     * 是否keepAlive
     */
    private Boolean keepAlive;

    /**
     * 是否在menu菜单中显示
     */
    private Boolean showLink;
}
