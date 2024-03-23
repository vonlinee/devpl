package io.devpl.backend.domain.param;

import io.devpl.backend.entity.DataTypeItem;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * 数据类型添加参数
 */
@Getter
@Setter
public class DataTypeAddParam {

    /**
     * 数据类型列表 批量添加
     */
    private Collection<DataTypeItem> dataTypeItems;
}
