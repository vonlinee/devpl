package io.devpl.backend.domain.vo;

/**
 * 下拉选择VO
 * 结合前端组件使用
 */
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

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SelectOptionVO{" +
            "key=" + key +
            ", label='" + label + '\'' +
            ", value=" + value +
            '}';
    }

    public SelectOptionVO(Object key, String label, Object value) {
        this.key = key;
        this.label = label;
        this.value = value;
    }
}
