package io.devpl.backend.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 下拉选择VO
 * 结合前端组件使用
 */
@Getter
@Setter
public class SelectionVO {

    /**
     * 每个选项的key
     */
    private String key;

    /**
     * 选项显示的文本
     */
    private String label;

    /**
     * 选项的值，前端组件选择某个选项时要传给后端的值即为此字段
     */
    private Object value;
}
