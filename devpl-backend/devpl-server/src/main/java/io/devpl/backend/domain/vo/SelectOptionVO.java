package io.devpl.backend.domain.vo;

import lombok.Data;

/**
 * 下拉选择VO
 * 结合前端组件使用
 */
@Data
public class SelectOptionVO {

    /**
     * 每个选项的key
     * 数字或者字符串
     */
    private Object key;

    /**
     * 选项显示的文本
     */
    private String label;

    /**
     * 选项的值，前端组件选择某个选项时要传给后端的值即为此字段
     */
    private Object value;

    public SelectOptionVO(Object key, String label, Object value) {
        this.key = key;
        this.label = label;
        this.value = value;
    }

    public SelectOptionVO(Object klv) {
        this.key = klv;
        this.label = String.valueOf(klv);
        this.value = klv;
    }
}
