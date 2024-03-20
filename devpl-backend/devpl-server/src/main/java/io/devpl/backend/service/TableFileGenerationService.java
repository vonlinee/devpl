package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.domain.param.TableFileGenParam;
import io.devpl.backend.entity.TableFileGeneration;

import java.util.List;

/**
 * 表文件生成记录表
 **/
public interface TableFileGenerationService extends IService<TableFileGeneration> {

    /**
     * 查询单表生成的文件
     *
     * @param tableId 表ID
     * @return 单表生成的文件
     */
    List<TableFileGeneration> listByTableId(Long tableId);

    List<TableFileGeneration> listByTableIds(Long[] tableIds);

    boolean removeByTableIds(Long[] tableIds);

    boolean logicRemoveByTableIds(Long[] tableIds);

    boolean updateGeneratedFiles(TableFileGenParam param);

    /**
     * 按表 ID 删除
     *
     * @param tableIds    表 ID
     * @param logicDelete 逻辑删除
     * @return boolean
     */
    boolean removeByTableIds(Long[] tableIds, boolean logicDelete);
}
