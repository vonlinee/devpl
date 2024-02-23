package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.entity.TemplateParam;
import io.devpl.sdk.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 模板参数Service
 */
public interface TemplateParamService extends IService<TemplateParam> {

    /**
     * 获取全局模板参数
     * 模板ID为NULL的为全局参数
     *
     * @return 全局模板参数
     */
    List<TemplateParam> getGlobalTemplateParams();

    /**
     * 获取全局模板参数 Map
     * 模板ID为NULL的为全局参数
     *
     * @return 全局模板参数 key为
     */
    default Map<String, TemplateParam> getGlobalTemplateParamsMap() {
        return CollectionUtils.toMap(getGlobalTemplateParams(), TemplateParam::getParamKey);
    }

    default Map<String, String> getGlobalTemplateParamValuesMap() {
        return CollectionUtils.toMap(getGlobalTemplateParams(), TemplateParam::getParamKey, TemplateParam::getParamValue);
    }

    /**
     * 获取全局模板参数
     * 模板ID为NULL的为全局参数
     *
     * @return 全局模板参数
     */
    List<TemplateParam> listTemplateParamsByTemplateId(Long templateId);
}
