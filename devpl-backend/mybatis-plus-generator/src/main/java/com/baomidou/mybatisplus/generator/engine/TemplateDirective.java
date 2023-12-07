package com.baomidou.mybatisplus.generator.engine;

public interface TemplateDirective {

    /**
     * 指令的名称
     *
     * @return 指令的名称
     */
    String getName();

    /**
     * 参数类型
     *
     * @return 参数类型列表
     */
    Class<?>[] getParameterTypes();

    /**
     * @param params 指令参数 对应{@code getParameterTypes}的参数
     * @return 输出的内容
     */
    String render(Object[] params);
}
