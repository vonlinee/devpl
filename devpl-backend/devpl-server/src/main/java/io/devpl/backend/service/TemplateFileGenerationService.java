package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.entity.TemplateFileGeneration;

public interface TemplateFileGenerationService extends IService<TemplateFileGeneration> {

    /**
     * TODO 渲染模板文件
     *
     * @param id 生成ID
     */
    void generate(Long id);
}
