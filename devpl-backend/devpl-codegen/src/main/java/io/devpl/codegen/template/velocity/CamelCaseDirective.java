package io.devpl.codegen.template.velocity;

import io.devpl.codegen.core.CaseFormat;

/**
 * userdirective=com.baomidou.mybatisplus.generator.engine.velocity.CamelCaseDirective
 * 注册自定义指令: velocity.properties
 * userdirective=继承了Directive类的子类全类名
 */
public class CamelCaseDirective extends VelocityTemplateDirective {

    /**
     * 函数名，也就是模板调用这个函数的名称，比如#hellofun()
     *
     * @return 函数名
     */
    @Override
    public String getName() {
        return "toCamelCase";
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return new Class[]{String.class};
    }

    @Override
    public String render(Object[] params) {
        return CaseFormat.CAPITAL_FIRST.normalize(CaseFormat.underlineToCamel((String) params[0]));
    }
}
