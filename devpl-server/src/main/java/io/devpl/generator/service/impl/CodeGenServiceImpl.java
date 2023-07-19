package io.devpl.generator.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import io.devpl.generator.common.exception.ServerException;
import io.devpl.generator.common.utils.DateUtils;
import io.devpl.generator.config.template.GeneratorConfig;
import io.devpl.generator.config.template.GeneratorInfo;
import io.devpl.generator.config.template.ProjectInfo;
import io.devpl.generator.domain.FileNode;
import io.devpl.generator.entity.GenBaseClass;
import io.devpl.generator.entity.GenTable;
import io.devpl.generator.entity.GenTableField;
import io.devpl.generator.entity.TemplateInfo;
import io.devpl.generator.service.*;
import io.devpl.generator.utils.ArrayUtils;
import io.devpl.generator.utils.SecurityUtils;
import io.devpl.sdk.io.FilenameUtils;
import io.devpl.sdk.util.IdUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成
 */
@Slf4j
@Service
public class CodeGenServiceImpl implements CodeGenService {

    @Resource
    private DataSourceService datasourceService;
    @Resource
    private FieldTypeService fieldTypeService;
    @Resource
    private BaseClassService baseClassService;
    @Resource
    private GeneratorConfig generatorConfig;
    @Resource
    private TableService tableService;
    @Resource
    private TableFieldService tableFieldService;
    @Resource
    private TemplateService templateService;

    // 代码生成器信息
    private GeneratorInfo generatorInfo;

    @PostConstruct
    public void initGeneratorConfig() {
        generatorInfo = generatorConfig.getGeneratorConfig();
    }

    @Override
    public GeneratorInfo getGeneratorInfo() {
        return generatorInfo;
    }

    @Override
    public void downloadCode(Long tableId, ZipOutputStream zip) {
        // 数据模型
        Map<String, Object> dataModel = getDataModel(tableId);
        // 渲染模板并输出
        for (TemplateInfo template : generatorInfo.getTemplates()) {
            dataModel.put("templateName", template.getTemplateName());
            String content = templateService.render(template.getContent(), dataModel);
            String path = templateService.render(template.getGeneratorPath(), dataModel);

            path = tableId + File.separator + path;

            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(path));
                IoUtil.writeUtf8(zip, false, content);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                throw new ServerException("模板写入失败：" + path, e);
            }
        }
    }

    /**
     * 生成某个表的文件
     * @param tableId gen_table主键
     */
    @Override
    public void generatorCode(Long tableId) {
        // 数据模型
        Map<String, Object> dataModel = getDataModel(tableId);
        ProjectInfo project = generatorInfo.getProject();
        // 渲染模板并输出
        for (TemplateInfo template : generatorInfo.getTemplates()) {
            dataModel.put("templateName", template.getTemplateName());
            String content = templateService.render(template.getContent(), dataModel);
            String path = templateService.render(template.getGeneratorPath(), dataModel);
            FileUtil.writeUtf8String(content, path);
        }
    }

    /**
     * 获取渲染的数据模型
     * @param tableId 表ID
     */
    @Override
    public Map<String, Object> getDataModel(Long tableId) {
        // 表信息
        GenTable table = tableService.getById(tableId);
        List<GenTableField> fieldList = tableFieldService.listByTableId(tableId);
        table.setFieldList(fieldList);

        // 数据模型
        Map<String, Object> dataModel = new HashMap<>();

        // 获取数据库类型
        String dbType = datasourceService.getDatabaseProductName(table.getDatasourceId());
        dataModel.put("dbType", dbType);

        // 项目信息
        dataModel.put("package", table.getPackageName());
        dataModel.put("packagePath", table.getPackageName().replace(".", File.separator));
        dataModel.put("version", table.getVersion());
        dataModel.put("moduleName", table.getModuleName());
        dataModel.put("ModuleName", StringUtils.upperFirst(table.getModuleName()));
        dataModel.put("functionName", table.getFunctionName());
        dataModel.put("FunctionName", StringUtils.upperFirst(table.getFunctionName()));
        dataModel.put("formLayout", table.getFormLayout());

        // 开发者信息
        dataModel.put("author", table.getAuthor());
        dataModel.put("email", table.getEmail());
        dataModel.put("datetime", DateUtils.nowDateTimeString());
        dataModel.put("date", DateUtils.nowDateString());

        // 设置字段分类
        setFieldTypeList(dataModel, table);

        // 设置基类信息
        setBaseClass(dataModel, table);

        // 导入的包列表
        Set<String> importList = fieldTypeService.getPackageByTableId(table.getId());
        dataModel.put("importList", importList);

        // 表信息
        dataModel.put("tableName", table.getTableName());
        dataModel.put("tableComment", table.getTableComment());
        dataModel.put("className", StringUtils.lowerFirst(table.getClassName()));
        dataModel.put("ClassName", table.getClassName());
        dataModel.put("fieldList", table.getFieldList());

        // 生成路径
        dataModel.put("backendPath", table.getBackendPath());
        dataModel.put("frontendPath", table.getFrontendPath());

        return dataModel;
    }

    /**
     * 设置基类信息
     * @param dataModel 数据模型
     * @param table     表
     */
    private void setBaseClass(Map<String, Object> dataModel, GenTable table) {
        if (table.getBaseclassId() == null) {
            return;
        }
        // 基类
        GenBaseClass baseClass = baseClassService.getById(table.getBaseclassId());
        baseClass.setPackageName(baseClass.getPackageName());
        dataModel.put("baseClass", baseClass);
        // 基类字段
        String[] fields = baseClass.getFields().split(",");
        // 标注为基类字段
        for (GenTableField field : table.getFieldList()) {
            if (ArrayUtils.contains(fields, field.getFieldName())) {
                field.setBaseField(true);
            }
        }
    }

    /**
     * 获取文件树
     * @param workPath 工作路径
     * @return 文件节点列表
     */
    @Override
    public List<FileNode> getFileTree(String workPath) {
        File root = new File(workPath);
        List<FileNode> node = new ArrayList<>();
        recursive(root, node);
        return node;
    }

    @Override
    public String getFileContent(String path) {
        return FileUtil.readString(path, StandardCharsets.UTF_8);
    }

    /**
     * 递归生成文件树
     * @param path 目录
     * @param node 保存节点
     */
    private void recursive(File path, List<FileNode> node) {
        if (path.isDirectory()) {
            File[] list = path.listFiles();
            assert list != null;
            for (File file : list) {
                FileNode fileNode = new FileNode();
                fileNode.setKey(SecurityUtils.base64Encode(file.getAbsolutePath()));
                fileNode.setLabel(file.getName());
                fileNode.setPath(file.getAbsolutePath());
                node.add(fileNode);
                if (file.isDirectory()) {
                    fileNode.setIsLeaf(false);
                    fileNode.setSelectable(false);
                    List<FileNode> children = new ArrayList<>();
                    fileNode.setChildren(children);
                    recursive(file, children);
                } else {
                    fileNode.setIsLeaf(true);
                    fileNode.setSelectable(true);

                    String extension = FilenameUtils.getExtension(file.getName());
                    if (!StringUtils.hasText(extension)) {
                        extension = "txt";
                    }
                    fileNode.setExtension(extension);
                }
            }
        } else {
            FileNode fileNode = new FileNode();
            fileNode.setKey(SecurityUtils.base64Encode(path.getAbsolutePath()));
            fileNode.setLabel(path.getName());
            fileNode.setIsLeaf(true);
            fileNode.setSelectable(true);
            node.add(fileNode);
        }
    }

    /**
     * 设置字段分类信息
     * @param dataModel 数据模型
     * @param table     表
     */
    private void setFieldTypeList(Map<String, Object> dataModel, GenTable table) {
        // 主键列表 (支持多主键)
        List<GenTableField> primaryList = new ArrayList<>();
        // 表单列表
        List<GenTableField> formList = new ArrayList<>();
        // 网格列表
        List<GenTableField> gridList = new ArrayList<>();
        // 查询列表
        List<GenTableField> queryList = new ArrayList<>();
        for (GenTableField field : table.getFieldList()) {
            if (field.isPrimaryKey()) {
                primaryList.add(field);
            }
            if (field.isFormItem()) {
                formList.add(field);
            }
            if (field.isGridItem()) {
                gridList.add(field);
            }
            if (field.isQueryItem()) {
                queryList.add(field);
            }
        }
        dataModel.put("primaryList", primaryList);
        dataModel.put("formList", formList);
        dataModel.put("gridList", gridList);
        dataModel.put("queryList", queryList);
    }

}
