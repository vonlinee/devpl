package io.devpl.codegen.config;

import io.devpl.codegen.template.TemplateArguments;
import lombok.Getter;
import lombok.Setter;

/**
 * 全局模板参数
 */
@Getter
@Setter
public class GlobalTemplateArguments implements TemplateArguments {

    private String templateName;
    private String nowDate;
    private String nowTime;
    private String author;
    private String createTime;

    @Override
    public void fill(TemplateArguments arguments) {
        arguments.add("templateName", this.templateName);
        arguments.add("nowDate", this.nowDate);
        arguments.add("nowTime", this.nowTime);
        arguments.add("author", this.author);
        arguments.add("createTime", this.createTime);
    }
}
