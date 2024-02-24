package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.domain.param.TableFileGenParam;
import io.devpl.backend.entity.TableFileGeneration;

import java.util.List;

/**
 * 表文件生成记录表
 *
 * @author vonlinee vonlinee@163.com
 * @since 1.0.0 2023-11-24
 **/
public interface TableFileGenerationService extends IService<TableFileGeneration> {

    List<TableFileGeneration> listByTableId(Long tableId);

    boolean updateFilesToBeGenerated(TableFileGenParam param);

    boolean removeByTableIds(Long[] tableIds, boolean logicDelete);
}
