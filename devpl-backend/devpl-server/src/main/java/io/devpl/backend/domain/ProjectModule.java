package io.devpl.backend.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ProjectModule {

    /**
     * 模块名称
     */
    private String name;

    private String groupId;

    /**
     * 坐标
     */
    private String artifactId;

    /**
     * 版本号
     */
    private String version;

    /**
     * 子模块
     */
    private List<ProjectModule> modules;

    public ProjectModule(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MavenArtifact{" +
            "groupId='" + groupId + '\'' +
            ", artifactId='" + artifactId + '\'' +
            ", version='" + version + '\'' +
            '}';
    }

    public final void addModule(String moduleName) {
        if (modules == null) {
            modules = new ArrayList<>();
        }
        modules.add(new ProjectModule(moduleName));
    }
}
