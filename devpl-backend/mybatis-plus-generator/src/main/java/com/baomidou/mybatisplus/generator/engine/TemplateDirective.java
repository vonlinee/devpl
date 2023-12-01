package com.baomidou.mybatisplus.generator.engine;

public interface TemplateDirective {

    /**
     * @param value 指令参数
     * @return 输出的内容
     */
    String render(Object value);
}
