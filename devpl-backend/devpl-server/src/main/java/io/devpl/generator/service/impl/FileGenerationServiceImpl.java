package io.devpl.generator.service.impl;

import io.devpl.generator.common.ServerException;
import io.devpl.generator.config.template.GeneratorInfo;
import io.devpl.generator.domain.FileNode;
import io.devpl.generator.entity.*;
import io.devpl.generator.service.*;
import io.devpl.generator.utils.ArrayUtils;
import io.devpl.generator.utils.DateTimeUtils;
import io.devpl.generator.utils.DateUtils;
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.io.IOUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成
 */
@Slf4j
@Service
public class FileGenerationServiceImpl implements FileGenerationService {

    @Resource
    private DataSourceService datasourceService;
    @Resource
    private GenFieldTypeService fieldTypeService;
    @Resource
    private BaseClassService baseClassService;
    @Resource
    private GenTableService tableService;
    @Resource
    private GenTableFieldService tableFieldService;
    @Resource
    private TemplateService templateService;
    @Resource
    private GeneratorConfigService generatorConfigService;
    @Resource
    private FileStorageService fileStorageService;
    @Resource
    private TableFileGenerationService tableFileGenerationService;
    @Resource
    private TemplateFileGenerationService templateFileGenerationService;

    /**
     * 代码生成根目录
     */
    @Value("${devpl.file.codegen.root}")
    private String codeGenRootDir;

    @Override
    public GeneratorInfo getGeneratorInfo() {
        return generatorConfigService.getGeneratorInfo(true);
    }

    @Override
    public void downloadCode(Long tableId, ZipOutputStream zip) {
        // 数据模型
        Map<String, Object> dataModel = prepareDataModel(tableId);

        GeneratorInfo generatorInfo = getGeneratorInfo();
        // 渲染模板并输出
        for (TemplateInfo template : generatorInfo.getTemplates()) {
            dataModel.put("templateName", template.getTemplateName());
            String content = templateService.render(template.getContent(), dataModel);
            String path = templateService.render(template.getGeneratorPath(), dataModel);
            path = tableId + File.separator + path;
            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(path));
                IOUtils.write(content, zip, StandardCharsets.UTF_8.name());
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                throw new ServerException("模板写入失败：" + path, e);
            }
        }
    }

    /**
     * 生成某个表的文件
     *
     * @param tableId gen_table主键
     * @return 生成文件的根目录 目录自定义 codeGenRootDir为根路径，前端不可见
     */
    @Override
    public String startCodeGeneration(Long tableId) {
        final String parentDirectory = tableId + "/" + DateTimeUtils.stringOfNow("yyyyMMddHHmmssSSS");

        List<TableFileGeneration> fileToBeGenerated = tableFileGenerationService.listByTableId(tableId);

        for (TableFileGeneration tfg : fileToBeGenerated) {
            templateFileGenerationService.generate(tfg.getGenerationId());
        }

        // 数据模型
        Map<String, Object> dataModel = prepareDataModel(tableId);
        GeneratorInfo generatorInfo = getGeneratorInfo();

        // 渲染模板并输出
        for (TemplateInfo template : generatorInfo.getTemplates()) {
            dataModel.put("templateName", template.getTemplateName());
            String content = templateService.render(template.getContent(), dataModel);
            // 文件保存路径
            String path = parentDirectory + "/" + templateService.render(template.getGeneratorPath(), dataModel);
            try {
                FileUtils.writeStringToFile(new File(codeGenRootDir, path), content, StandardCharsets.UTF_8.name());
            } catch (Exception exception) {
                log.error("写入模板失败{}", template.getTemplateName());
            }
        }
        return parentDirectory;
    }

    /**
     * 获取渲染的数据模型
     *
     * @param tableId 表ID
     */
    @Override
    public Map<String, Object> prepareDataModel(Long tableId) {
        // 表信息
        GenTable table = tableService.getById(tableId);
        List<GenTableField> fieldList = tableFieldService.listByTableId(tableId);
        table.setFieldList(fieldList);

        // 数据模型
        Map<String, Object> dataModel = new HashMap<>();

        // 获取数据库类型
        dataModel.put("dbType", datasourceService.getDatabaseProductName(table.getDatasourceId()));

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

        // 前后端生成路径
        dataModel.put("backendPath", table.getBackendPath());
        dataModel.put("frontendPath", table.getFrontendPath());

        return dataModel;
    }

    /**
     * 设置基类信息
     *
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
     *
     * @param workPath 工作路径
     * @return 文件节点列表
     */
    @Override
    public List<FileNode> getGeneratedFileTree(String workPath) {
        File root = new File(codeGenRootDir, workPath);
        return fileStorageService.getFileTree(root.getAbsolutePath());
    }

    @Override
    public String getFileContent(String path) {
        Path filepath = Path.of(path);
        if (Files.exists(filepath)) {
            try {
                return FileUtils.readUTF8String(new File(path));
            } catch (Exception exception) {
                throw ServerException.create("读取文件%s失败", path, exception.getMessage());
            }
        }
        throw ServerException.create("文件%s不存在", path);
    }

    /**
     * 设置字段分类信息
     *
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
