package io.devpl.generator.domain.param;

import io.devpl.generator.entity.TableFileGeneration;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TableFileGenParam {

    /**
     * 生成表ID
     */
    private Long tableId;

    /**
     * 生成文件配置信息
     */
    private List<TableFileGeneration> fileInfoList;
}
