package io.devpl.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.domain.param.ModelListParam;
import io.devpl.backend.entity.ModelInfo;

import java.util.Collection;

/**
 * 基类管理
 */
public interface DomainModelService extends IService<ModelInfo> {

    ModelInfo getModelInfo(Long modelId);

    IPage<ModelInfo> listPage(ModelListParam param);

    ModelInfo getById(ModelListParam param);

    boolean saveModel(ModelInfo modelInfo);

    boolean removeField(Long modelId, Collection<Long> fieldIds);

    boolean updateModel(ModelInfo modelInfo);

    /**
     * 添加模型关联的字段信息
     *
     * @param modelId  模型ID
     * @param fieldIds 字段ID列表
     * @return 是否成功
     */
    boolean addFieldsForModel(Long modelId, Collection<Long> fieldIds);
}
