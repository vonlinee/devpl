package io.devpl.backend.domain.param;

import io.devpl.backend.entity.FieldInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JavaPojoCodeGenParam {

    /**
     * 生成类型
     */
    @NotNull(message = "生成类型不能为空")
    private Integer type;

    private String className;

    private String packageName;

    /**
     * 是否使用lombok
     */
    private Boolean useLombok;

    /**
     * 是否生成getter/setter
     */
    private Boolean setterAndGetter;

    /**
     * 字段信息
     */
    private List<FieldInfo> fields;

    public boolean useLombok() {
        return useLombok == null || useLombok;
    }
}
