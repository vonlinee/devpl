package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.entity.TableFileGeneration;
import io.devpl.backend.entity.TemplateFileGeneration;

import java.util.Map;

public interface TemplateFileGenerationService extends IService<TemplateFileGeneration> {

    /**
     * TODO 渲染模板文件
     *
     * @param id 生成ID
     */
    void generate(Long id);

    void saveTemplateArguments(TableFileGeneration generation, Map<String, Object> arguments);
}
