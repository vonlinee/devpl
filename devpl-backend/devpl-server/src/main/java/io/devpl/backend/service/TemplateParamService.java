package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.entity.TemplateParam;

import java.util.List;

/**
 * 模板参数Service
 */
public interface TemplateParamService extends IService<TemplateParam> {
    List<TemplateParam> getGlobalTemplateParams();
}
