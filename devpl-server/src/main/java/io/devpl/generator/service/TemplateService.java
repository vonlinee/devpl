package io.devpl.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.generator.entity.TemplateInfo;

import java.util.Map;

/**
 * 模板服务
 */
public interface TemplateService {

    /**
     * 渲染模板
     * @param template  模板内容
     * @param dataModel 数据模型
     * @return 渲染后的模板
     */
    String render(String template, Map<String, Object> dataModel);

    /**
     * @return 分页数据
     */
    IPage<TemplateInfo> pages(int pageIndex, int pageSize);
}
