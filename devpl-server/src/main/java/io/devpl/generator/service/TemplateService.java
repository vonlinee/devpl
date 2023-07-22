package io.devpl.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.generator.domain.vo.TemplateSelectVO;
import io.devpl.generator.entity.TemplateInfo;

import java.util.List;
import java.util.Map;

/**
 * 模板服务
 */
public interface TemplateService extends IService<TemplateInfo> {

    /**
     * 保存模板
     * @param templateInfo 模板信息
     * @return 是否成功
     */
    boolean save(TemplateInfo templateInfo);

    /**
     * 渲染模板
     * @param template  模板内容
     * @param dataModel 数据模型
     * @return 渲染后的模板
     */
    String render(String template, Map<String, Object> dataModel);

    /**
     * 分页查询
     * @param pageIndex 第几页
     * @param pageSize  每页大小
     * @return 分页数据
     */
    IPage<TemplateInfo> pages(int pageIndex, int pageSize);

    List<TemplateSelectVO> listSelectableTemplates();
}
