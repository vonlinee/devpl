package io.devpl.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.domain.param.TemplateInfoListParam;
import io.devpl.backend.domain.vo.TemplateProviderVO;
import io.devpl.backend.domain.vo.TemplateSelectVO;
import io.devpl.backend.entity.TemplateInfo;
import io.devpl.backend.entity.TemplateVariableMetadata;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.TemplateException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * 模板 Service
 */
public interface TemplateService extends IService<TemplateInfo> {

    /**
     * 所有模板类型列表
     *
     * @return 模板类型列表
     */
    List<TemplateProviderVO> listTemplateTypes();

    /**
     * 保存模板
     *
     * @param templateInfo 模板信息
     * @return 是否成功
     */
    boolean addTemplate(TemplateInfo templateInfo);

    /**
     * 渲染模板ID
     *
     * @param templateId 模板ID
     * @param dataModel  模板参数
     * @param out        输出位置
     * @see TemplateService#render(TemplateInfo, Map, Writer)
     */
    void render(Long templateId, Map<String, Object> dataModel, Writer out);

    /**
     * 渲染模板
     *
     * @param templateInfo 模板信息
     * @param dataModel    模板参数
     * @param out          输出位置
     */
    void render(@NotNull TemplateInfo templateInfo, Map<String, Object> dataModel, Writer out) throws TemplateException;

    /**
     * 模板渲染 直接渲染文件模板
     *
     * @param template  模板文件
     * @param dataModel 模板数据
     * @param out       输出位置
     */
    void render(File template, Map<String, Object> dataModel, Writer out);

    /**
     * 渲染模板，用于模板内容比较短的情况
     *
     * @param template  模板内容
     * @param dataModel 数据模型
     * @return 结果字符串
     */
    String render(TemplateEngine engine, String template, Map<String, Object> dataModel);

    /**
     * 分页查询
     *
     * @return 分页数据
     */
    IPage<TemplateInfo> listPageTemplates(TemplateInfoListParam param);

    /**
     * 模板选择列表
     *
     * @return 模板选择列表
     */
    List<TemplateSelectVO> listSelectable();

    /**
     * 查询所有系统预设模板信息
     *
     * @return 系统预设模板列表
     */
    List<TemplateInfo> listInternalTemplates();

    /**
     * 模板迁移，包含模板文件迁移及相应的模板信息
     */
    void migrateTemplates();

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
    List<TemplateVariableMetadata> introspect(TemplateInfo templateInfo);
}
