package io.devpl.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.generator.domain.vo.TemplateSelectVO;
import io.devpl.generator.entity.TemplateInfo;
import io.devpl.generator.entity.TemplateVarInfo;

import java.util.List;
import java.util.Map;

/**
 * 模板 Service
 */
public interface TemplateService extends IService<TemplateInfo> {

    /**
     * 保存模板
     *
     * @param templateInfo 模板信息
     * @return 是否成功
     */
    boolean addTemplate(TemplateInfo templateInfo);

    /**
     * 渲染模板
     *
     * @param template  模板内容
     * @param dataModel 数据模型
     * @return 渲染后的模板
     */
    String render(String template, Map<String, Object> dataModel);

    /**
     * 分页查询
     *
     * @param pageIndex 第几页
     * @param pageSize  每页大小
     * @return 分页数据
     */
    IPage<TemplateInfo> listPageTemplates(int pageIndex, int pageSize);

    /**
     * 模板选择列表
     *
     * @return 模板选择列表
     */
    List<TemplateSelectVO> listSelectable();

    /**
     * 模板迁移，包含模板文件迁移及相应的模板信息
     *
     * @return 是否成功
     */
    boolean migrateTemplates();

    /**
     * 将模板文件路径转换为平台独立的模板存放路径
     *
     * @param templatePath 模板路径
     * @return 平台独立的文件路径
     */
    String formatTemplatePath(String templatePath);

    /**
     * 提取模板中的变量信息
     *
     * @param templateInfo 模板信息
     * @return 模板变量列表
     */
    List<TemplateVarInfo> introspect(TemplateInfo templateInfo);
}
