package io.devpl.backend.system;

import lombok.Data;

import java.util.List;

@Data
public class Route {

    /**
     * 路径
     */
    private String path;

    /**
     * 名称
     */
    private String name;

    /**
     * 元数据
     */
    private RouteMeta meta;

    /**
     * 子路由
     */
    private List<Route> children;
}
