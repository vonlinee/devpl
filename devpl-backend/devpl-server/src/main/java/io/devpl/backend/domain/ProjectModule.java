package io.devpl.backend.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private String packageWay;

    /**
     * 根路径，项目目录
     */
    private String rootPath;

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

    public boolean hasModules() {
        return modules != null && !modules.isEmpty();
    }

    public void merge(ProjectModule module) {
        if (Objects.equals(this.name, module.getName())) {
            this.groupId = module.getGroupId();
            this.artifactId = module.getArtifactId();
            this.version = module.getVersion();
            this.packageWay = module.getPackageWay();
            this.modules = module.getModules();
            this.rootPath = module.getRootPath();
        }
    }
}
