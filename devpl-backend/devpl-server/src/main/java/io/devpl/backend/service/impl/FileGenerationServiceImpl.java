package io.devpl.backend.service.impl;

import io.devpl.backend.boot.CodeGenProperties;
import io.devpl.backend.common.exception.BusinessException;
import io.devpl.backend.domain.bo.TableImportInfo;
import io.devpl.backend.domain.param.FileGenerationParam;
import io.devpl.backend.domain.vo.FileGenerationResult;
import io.devpl.backend.entity.TableFileGeneration;
import io.devpl.backend.entity.TableGeneration;
import io.devpl.backend.entity.TemplateInfo;
import io.devpl.backend.service.*;
import io.devpl.backend.utils.DateTimeUtils;
import io.devpl.backend.utils.PathUtils;
import io.devpl.common.model.FileNode;
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.util.CollectionUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 代码文件生成
 */
@Slf4j
@Service
public class FileGenerationServiceImpl implements FileGenerationService {

    @Resource
    private TableGenerationService tableGenerationService;
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
    private CodeGenProperties codeGenProperties;

    /**
     * 生成文件
     *
     * @param param 参数
     * @return {@link FileGenerationResult}
     */
    @Override
    public FileGenerationResult generateFiles(FileGenerationParam param) {
        FileGenerationResult result = new FileGenerationResult(null);
        // 生成代码
        final String parentDirectory = DateTimeUtils.stringOfNow("yyyyMMddHHmmssSSS");
        List<TableGeneration> tableGenerations = tableGenerationService.listByIds(param.getTableIds());
        for (TableGeneration tableGeneration : tableGenerations) {
            result.addRootDir(this.generateForTable(tableGeneration, parentDirectory));
        }
        return result;
    }

    /**
     * 生成某个表的文件
     *
     * @param table           table_file_generation信息
     * @param parentDirectory 根目录
     * @return 生成文件的根目录 目录自定义 codeGenRootDir为根路径，前端不可见
     * @see TableGenerationService#importSingleTable(TableImportInfo)
     */
    @Override
    public String generateForTable(TableGeneration table, String parentDirectory) {
        // 单张表需要生成的文件列表
        List<TableFileGeneration> fileToBeGenerated = tableFileGenerationService.listByTableId(table.getId());
        if (CollectionUtils.isEmpty(fileToBeGenerated)) {
            return parentDirectory;
        }
        table.setGenerationFiles(fileToBeGenerated);
        table.setFieldList(tableFieldService.listByTableId(table.getId()));

        if (CollectionUtils.isEmpty(table.getTemplateArguments())) {
            tableGenerationService.initTableTemplateArguments(table);
        }

        doGenerateForTable(table, parentDirectory);
        return parentDirectory;
    }

    private void doGenerateForTable(TableGeneration table, String parentDirectory) {
        Map<String, Object> dataModel = table.getTemplateArguments();
        // 使用的模板ID
        Set<Long> templateIds = CollectionUtils.toSet(table.getGenerationFiles(), TableFileGeneration::getTemplateId);
        // 使用的模板列表
        List<TemplateInfo> templates = templateService.listByIds(templateIds);

        final Map<Long, TemplateInfo> templateInfoMap = CollectionUtils.toMap(templates, TemplateInfo::getTemplateId);

        // 生成该表需要生成的所有文件
        for (TableFileGeneration tfg : table.getGenerationFiles()) {
            dataModel.put("templateName", tfg.getTemplateName());
            templateFileGenerationService.saveTemplateFileGenerationArguments(tfg, dataModel);
            String saveLocation = this.getAbsolutePath(PathUtils.of(parentDirectory, tfg.getSavePath(), tfg.getFileName()));
            File file = new File(saveLocation);
            FileUtils.createFileQuietly(file, true);
            if (file.exists()) {
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
    }

    @Override
    public String getAbsolutePath(String path) {
        return codeGenProperties.getCodeGenRootDir() + "/" + path;
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
}
