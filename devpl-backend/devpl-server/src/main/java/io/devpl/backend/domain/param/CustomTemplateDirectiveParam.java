package io.devpl.backend.domain.param;

import lombok.Data;

@Data
public class CustomTemplateDirectiveParam {

    /**
     * 指令名称
     */
    private String directiveId;

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
}
