package io.devpl.backend.service.impl;

import io.devpl.backend.boot.CodeGenProperties;
import io.devpl.backend.common.exception.BusinessException;
import io.devpl.backend.domain.FileNode;
import io.devpl.backend.domain.param.FileGenerationParam;
import io.devpl.backend.domain.param.TableImportParam;
import io.devpl.backend.domain.vo.FileGenerationResult;
import io.devpl.backend.entity.*;
import io.devpl.backend.service.*;
import io.devpl.backend.utils.DateTimeUtils;
import io.devpl.backend.utils.PathUtils;
import io.devpl.backend.utils.ProjectUtils;
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.util.ArrayUtils;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * 代码生成
 */
@Slf4j
@Service
public class FileGenerationServiceImpl implements FileGenerationService {

    @Resource
    private RdbmsConnectionInfoService datasourceService;
    @Resource
    private DomainModelService baseClassService;
    @Resource
    private TableGenerationService tableService;
    @Resource
    private TableGenerationFieldService tableFieldService;
    @Resource
    private TemplateService templateService;
    @Resource
    private FileStorageService fileStorageService;
    @Resource
    private TableFileGenerationService tableFileGenerationService;
    @Resource
    private TemplateFileGenerationService templateFileGenerationService;
    @Resource
    private TemplateArgumentService templateArgumentService;
    @Resource
    private TemplateParamService templateParamService;
    @Resource
    private CodeGenProperties codeGenProperties;

    @Override
    public FileGenerationResult generateFile(FileGenerationParam param) {
        FileGenerationResult result = new FileGenerationResult(null);
        // 生成代码
        final String parentDirectory = DateTimeUtils.stringOfNow("yyyyMMddHHmmssSSS");
        for (Long tableId : param.getTableIds()) {
            result.addRootDir(this.generateForTable(tableId, parentDirectory));
        }
        return result;
    }

    /**
     * 生成某个表的文件
     *
     * @param tableId gen_table主键
     * @return 生成文件的根目录 目录自定义 codeGenRootDir为根路径，前端不可见
     * @see TableGenerationService#importSingleTable(TableImportParam)
     */
    @Override
    public String generateForTable(Long tableId, String parentDirectory) {
        List<TableFileGeneration> fileToBeGenerated = tableFileGenerationService.listByTableId(tableId);
        // 数据模型
        Map<String, Object> dataModel = prepareDataModel(tableId);

        StringBuilder sb = new StringBuilder();

        final Map<Long, TemplateInfo> templateInfoMap = CollectionUtils.toMap(templateService.listByIds(CollectionUtils.toSet(fileToBeGenerated, TableFileGeneration::getTemplateId)), TemplateInfo::getTemplateId);

        for (TableFileGeneration tfg : fileToBeGenerated) {
            dataModel.put("templateName", tfg.getTemplateName());

            String saveLocation = PathUtils.of(parentDirectory, tfg.getSavePath(), tfg.getFileName());

            saveLocation = this.getAbsolutePath(saveLocation);
            sb.append("模板").append(tfg.getTemplateName()).append("\t => ").append(saveLocation).append("\n");
            File file = new File(saveLocation);

            FileUtils.createFileQuietly(file, true);
            if (file.exists()) {
                sb.append("\t创建文件成功 ").append(file.getAbsolutePath()).append("\n");

                TemplateInfo templateInfo = templateInfoMap.get(tfg.getTemplateId());

                if (templateInfo != null) {
                    try (Writer writer = new FileWriter(file)) {
                        templateService.render(templateInfo, dataModel, writer);
                    } catch (Exception e) {
                        log.error("渲染{}失败", tfg.getFileName(), e);
                    }
                }
            }
        }

        log.info("生成日志 \n{}", sb);

        return parentDirectory;
    }

    @Override
    public String getAbsolutePath(String path) {
        return codeGenProperties.getCodeGenRootDir() + "/" + path;
    }

    /**
     * 获取渲染的数据模型
     *
     * @param tableId 表ID
     */
    @Override
    public Map<String, Object> prepareDataModel(Long tableId) {
        // 表信息
        TableGeneration table = tableService.getById(tableId);
        List<TableGenerationField> fieldList = tableFieldService.listByTableId(tableId);
        table.setFieldList(fieldList);

        // 数据模型
        Map<String, Object> dataModel = new HashMap<>();

        // 获取数据库类型
        dataModel.put("dbType", datasourceService.getDatabaseProductName(table.getDatasourceId()));
        dataModel.put("entity", table.getClassName());

        // 包名配置
        Map<String, Object> packageConfig = new HashMap<>();
        packageConfig.put("Controller", table.getPackageName() + ".controller");
        packageConfig.put("Mapper", table.getPackageName() + ".mapper");
        packageConfig.put("Service", table.getPackageName() + ".service");
        packageConfig.put("Entity", table.getPackageName() + ".entity");
        packageConfig.put("ServiceImpl", table.getPackageName() + ".service.impl");

        dataModel.put("package", packageConfig);

        // 表配置信息
        Map<String, Object> tableConfig = new HashMap<>();
        tableConfig.put("entityPath", table.getTableName());
        tableConfig.put("controllerName", table.getClassName() + ".Controller");
        dataModel.put("table", tableConfig);

        // 项目信息
        dataModel.put("packagePath", table.getPackageName().replace(".", File.separator));
        dataModel.put("version", table.getVersion());
        dataModel.put("moduleName", table.getModuleName());
        dataModel.put("ModuleName", StringUtils.upperFirst(ProjectUtils.toSimpleIdentifier(table.getModuleName())));
        dataModel.put("functionName", table.getFunctionName());
        dataModel.put("FunctionName", StringUtils.upperFirst(table.getFunctionName()));
        dataModel.put("formLayout", table.getFormLayout());

        // 开发者信息
        dataModel.put("author", table.getAuthor());
        dataModel.put("email", table.getEmail());
        dataModel.put("datetime", DateTimeUtils.stringOfNow());
        dataModel.put("date", DateTimeUtils.nowDateString());

        // 设置字段分类
        setFieldTypeList(dataModel, table);

        // 设置基类信息
        setBaseClass(dataModel, table);

        // 导入的包列表
        Set<String> importList = new HashSet<>();
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

        // 全局模板参数
        for (TemplateParam param : templateParamService.getGlobalTemplateParams()) {
            dataModel.put(param.getParamKey(), param.getParamValue());
        }

        return dataModel;
    }

    /**
     * 设置基类信息
     *
     * @param dataModel 数据模型
     * @param table     表
     */
    private void setBaseClass(Map<String, Object> dataModel, TableGeneration table) {
        if (table.getBaseclassId() == null) {
            return;
        }
        // 基类
        ModelInfo baseClass = baseClassService.getById(table.getBaseclassId());
        baseClass.setPackageName(baseClass.getPackageName());
        dataModel.put("baseClass", baseClass);
        // 基类字段
        String[] fields = baseClass.getFieldsName().split(",");
        // 标注为基类字段
        for (TableGenerationField field : table.getFieldList()) {
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
        File root = new File(codeGenProperties.getCodeGenRootDir(), workPath);
        if (!root.exists()) {
            return Collections.emptyList();
        }
        return fileStorageService.getFileTree(root.getAbsolutePath());
    }

    @Override
    public String getFileContent(String path) {
        Path filepath = Path.of(path);
        if (Files.exists(filepath)) {
            try {
                return FileUtils.readUTF8String(new File(path));
            } catch (Exception exception) {
                throw BusinessException.create("读取文件%s失败", path, exception.getMessage());
            }
        }
        return String.format("文件%s不存在", path);
    }

    /**
     * 设置字段分类信息
     *
     * @param dataModel 数据模型
     * @param table     表
     */
    private void setFieldTypeList(Map<String, Object> dataModel, TableGeneration table) {
        // 主键列表 (支持多主键)
        List<TableGenerationField> primaryList = new ArrayList<>();
        // 表单列表
        List<TableGenerationField> formList = new ArrayList<>();
        // 网格列表
        List<TableGenerationField> gridList = new ArrayList<>();
        // 查询列表
        List<TableGenerationField> queryList = new ArrayList<>();
        for (TableGenerationField field : table.getFieldList()) {
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
