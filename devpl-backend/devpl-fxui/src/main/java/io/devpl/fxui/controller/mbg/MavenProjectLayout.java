package io.devpl.fxui.controller.mbg;

/**
 * Maven项目结构
 */
public class MavenProjectLayout implements ProjectLayout {

    /**
     * 项目根路径
     */
    private String projectRoot;

    @Override
    public void setProjectRoot(String root) {
        this.projectRoot = root;
    }

    @Override
    public String getProjectRoot() {
        return projectRoot;
    }

    @Override
    public String locate(Object... args) {
        return null;
    }

    @Override
    public boolean check(Object... args) {
        return false;
    }
}
