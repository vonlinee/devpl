package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.entity.TableFileGeneration;
import io.devpl.backend.entity.TemplateFileGeneration;

import java.util.Map;

public interface TemplateFileGenerationService extends IService<TemplateFileGeneration> {

    /**
     * 保存模板文件的模板参数
     *
     * @param generation 表生成信息
     * @param arguments  单个模板参数
     */
    boolean saveTemplateFileGenerationArguments(TableFileGeneration generation, Map<String, Object> arguments);
}
