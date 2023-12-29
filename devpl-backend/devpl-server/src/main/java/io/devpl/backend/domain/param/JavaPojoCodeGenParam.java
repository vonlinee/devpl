package io.devpl.backend.domain.param;

import io.devpl.backend.entity.FieldInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JavaPojoCodeGenParam {

    private String className;

    private String packageName;

    /**
     * 是否使用lombok
     */
    private Boolean useLombok;

    /**
     * 字段信息
     */
    List<FieldInfo> fields;

    public boolean useLombok() {
        return useLombok == null || useLombok;
    }
}
