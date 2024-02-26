package io.devpl.backend.domain.vo;

import io.devpl.backend.entity.FieldInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 字段解析结果
 */
@Getter
@Setter
public class FieldParseResult {

    /**
     * 是否失败
     */
    private boolean failed;

    /**
     * 解析错误信息
     */
    private String errorMsg;

    /**
     * 解析字段信息
     */
    private List<FieldInfo> fields;

    public final FieldParseResult fail(String errorMsg) {
        this.failed = true;
        this.errorMsg = errorMsg;
        this.fields = List.of();
        return this;
    }
}
