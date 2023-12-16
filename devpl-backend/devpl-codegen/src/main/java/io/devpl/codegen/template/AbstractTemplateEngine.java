package io.devpl.codegen.template;

import io.devpl.codegen.config.Context;

import java.io.OutputStream;
import java.util.Map;

/**
 * 模板引擎抽象类
 */
public abstract class AbstractTemplateEngine implements TemplateEngine {

    /**
     * 模板引擎初始化
     */
    public abstract AbstractTemplateEngine init(Context context);

    /**
     * 将模板转化成为文件
     *
     * @param objectMap    渲染对象 MAP 信息
     * @param templatePath 模板文件
     * @param outputStream 文件输出位置
     * @throws Exception 异常
     * @since 3.5.0
     */
    public abstract void merge(Map<String, Object> objectMap, String templatePath, OutputStream outputStream) throws Exception;

    /**
     * 模板真实文件路径
     *
     * @param filePath 文件路径
     * @return ignore
     */
    public abstract String templateFilePath(String filePath);

    @Override
    public String render(TemplateSource template, TemplateArguments arguments) {
        return null;
    }

    @Override
    public String render(String template, TemplateArguments arguments) {
        return null;
    }

    @Override
    public void render(TemplateSource template, TemplateArguments arguments, OutputStream outputStream) {

    }

    @Override
    public boolean registerDirective(TemplateDirective directive) {
        return false;
    }
}
