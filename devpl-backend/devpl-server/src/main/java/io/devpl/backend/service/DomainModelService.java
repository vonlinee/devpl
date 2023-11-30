package io.devpl.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.domain.param.BaseClassListParam;
import io.devpl.backend.entity.ModelInfo;

import java.util.List;

/**
 * 基类管理
 */
public interface DomainModelService extends IService<ModelInfo> {

    ModelInfo getModelInfo(Long modelId);

    IPage<ModelInfo> listPage(BaseClassListParam param);

    ModelInfo getById(BaseClassListParam param);

    boolean saveModel(ModelInfo modelInfo);

    boolean updateModel(ModelInfo modelInfo);

    boolean addModelFields(Long modelId, List<Long> fieldIds);
}
