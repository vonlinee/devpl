package io.devpl.fxui.controller.mbg;

/**
 * 项目布局：确定什么文件该存放在什么位置
 */
public interface ProjectLayout {

    /**
     * 设置项目根目录
     *
     * @param root 项目根目录
     */
    void setProjectRoot(String root);

    /**
     * 获取项目根目录
     */
    String getProjectRoot();

    /**
     * @param args 定义成可变参数增加灵活性
     * @return 存放的目录
     */
    String locate(Object... args);

    /**
     * 检查参数是否合法
     *
     * @param args 定义成可变参数增加灵活性
     * @return 是否可以调用locate方法
     */
    boolean check(Object... args);
}
