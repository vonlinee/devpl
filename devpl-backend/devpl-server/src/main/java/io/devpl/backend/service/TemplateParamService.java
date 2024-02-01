package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.entity.TemplateParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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
     * 获取全局模板参数
     * 模板ID为NULL的为全局参数
     *
     * @return 全局模板参数
     */
    List<TemplateParam> listTemplateParamsByTemplateId(Long templateId);
}
