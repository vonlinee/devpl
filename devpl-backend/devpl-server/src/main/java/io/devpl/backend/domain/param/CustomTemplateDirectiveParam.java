package io.devpl.backend.domain.param;

import lombok.Data;

@Data
public class CustomTemplateDirectiveParam {

    /**
     * 指令名称
     */
    private Long directiveId;

    /**
     * 指令名称
     */
    private String directiveName;

    /**
     * 指令实现代码
     */
    private String sourceCode;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 自定义指令实现类名
     */
    private String className;
}
