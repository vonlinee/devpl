package io.devpl.backend.domain.param;

import io.devpl.backend.entity.TableFileGeneration;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TableFileGenParam {

    /**
     * 逻辑删除参数
     */
    private boolean lr = true;

    /**
     * 生成表ID
     */
    private Long tableId;

    /**
     * 生成文件配置信息
     */
    private List<TableFileGeneration> fileInfoList;
}
