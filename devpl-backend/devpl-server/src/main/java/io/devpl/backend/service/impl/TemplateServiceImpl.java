package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.boot.CodeGenProperties;
import io.devpl.backend.common.exception.BusinessException;
import io.devpl.backend.dao.TemplateInfoMapper;
import io.devpl.backend.domain.enums.TemplateEngineType;
import io.devpl.backend.domain.param.TemplateInfoListParam;
import io.devpl.backend.domain.vo.TemplateProviderVO;
import io.devpl.backend.domain.vo.TemplateSelectVO;
import io.devpl.backend.entity.TemplateInfo;
import io.devpl.backend.entity.TemplateParam;
import io.devpl.backend.entity.TemplateVariableMetadata;
import io.devpl.backend.service.TemplateService;
import io.devpl.codegen.template.Template;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.TemplateException;
import io.devpl.codegen.template.beetl.BeetlTemplateEngine;
import io.devpl.codegen.template.enjoy.JFinalEnjoyTemplateEngine;
import io.devpl.codegen.template.freemarker.FreeMarkerTemplateEngine;
import io.devpl.codegen.template.velocity.VelocityTemplateEngine;
import io.devpl.sdk.annotations.NotNull;
import io.devpl.sdk.annotations.Nullable;
import io.devpl.sdk.annotations.Readonly;
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.io.FilenameUtils;
import io.devpl.sdk.io.IOUtils;
import io.devpl.sdk.lang.RuntimeIOException;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.IdUtils;
import io.devpl.sdk.util.ResourceUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模板服务 实现类
 */
@Slf4j
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateInfoMapper, TemplateInfo> implements TemplateService {

    @Resource
    TemplateInfoMapper templateInfoMapper;
    @Resource
    CodeGenProperties codeGenProperties;

    Map<TemplateEngineType, TemplateEngine> templateEngineMap = new HashMap<>();

    @PostConstruct
    public void init() {
        templateEngineMap.put(TemplateEngineType.BEETL, new BeetlTemplateEngine());
        templateEngineMap.put(TemplateEngineType.VELOCITY, new VelocityTemplateEngine());
        templateEngineMap.put(TemplateEngineType.FREE_MARKER, new FreeMarkerTemplateEngine());
        templateEngineMap.put(TemplateEngineType.ENJOY, new JFinalEnjoyTemplateEngine());
    }

    /**
     * p
     * 获取模板类型
     *
     * @return 模板类型列表
     */
    @Override
    public List<TemplateProviderVO> listTemplateTypes() {
        return Arrays.stream(TemplateEngineType.values()).map(tp -> {
            TemplateProviderVO vo = new TemplateProviderVO();
            vo.setProvider(tp.getProvider());
            vo.setProviderName(tp.getProviderName());
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 保存模板
     *
     * @param templateInfo 模板信息
     * @return 是否成功
     */
    @Override
    public boolean addTemplate(TemplateInfo templateInfo) {
        templateInfo.setUpdateTime(LocalDateTime.now());
        templateInfo.setCreateTime(LocalDateTime.now());
        templateInfo.setDeleted(false);
        if (templateInfo.isFileTemplate() && StringUtils.hasText(templateInfo.getContent())) {
            // 将模板保存到本地文件
            String extension = FilenameUtils.getExtension(templateInfo.getTemplateName());
            if (extension == null || extension.isEmpty()) {
                extension = "";
            } else {
                extension = "." + extension;
            }
            TemplateEngineType engineType = TemplateEngineType.findByProvider(templateInfo.getProvider());
            if (engineType != null) {
                extension = "." + engineType.getExtension();
            }
            String templateFileName = templateInfo.getTemplateName() + "_" + IdUtils.simple32UUID() + extension;
            Path dir = getTemplateSaveLocation(null, "custom");
            Path path = Paths.get(dir.toString(), templateFileName);
            if (!Files.exists(path)) {
                FileUtils.createFileQuietly(path, true);
            }
            File file = path.toFile();
            FileUtils.writeString(file, templateInfo.getContent());
            templateInfo.setTemplateFilePath(formatTemplatePath(file.getAbsolutePath()));
        }
        return templateInfoMapper.insert(templateInfo) == 1;
    }

    @Override
    public void render(Long templateId, Map<String, Object> dataModel, Writer out) {
        TemplateInfo templateInfo = getById(templateId);
        render(templateInfo, dataModel, out);
    }

    /**
     * 渲染单个模板，支持多种类型的模板渲染
     *
     * @param templateInfo 模板信息
     * @param dataModel    模板参数
     * @param out          输出位置
     */
    @Override
    public void render(@NotNull @org.jetbrains.annotations.NotNull TemplateInfo templateInfo, Map<String, Object> dataModel, Writer out) throws TemplateException {
        TemplateEngineType engineType = TemplateEngineType.findByProvider(templateInfo.getProvider());
        if (engineType == null) {
            throw new TemplateException("未注册的模板类型" + templateInfo.getProvider());
        }
        TemplateEngine engine = templateEngineMap.get(engineType);
        if (engine == null) {
            throw new TemplateException("未找到模板引擎实现" + templateInfo.getProvider());
        }
        try {
            // 渲染模板
            Template template = Template.UNKNOWN;
            if (templateInfo.isFileTemplate()) {
                File file = new File(templateInfo.getTemplateFilePath());
                if (file.exists()) {
                    String templateContent = FileUtils.readStringQuietly(file);
                    template = engine.getTemplate(templateContent, true);
                }
            } else {
                template = engine.getTemplate(templateInfo.getContent(), true);
            }
            if (template.exists()) {
                template.render(engine, dataModel, out);
            }
        } catch (TemplateException e) {
            // TODO 渲染失败，去掉某些堆栈信息
            IOUtils.writeQuietly(out, e.getMessage());
        }
    }

    private void renderTemplateString(TemplateEngine engine, String templateContent, Map<String, Object> dataModel, Writer out) {
        try {
            // 渲染模板
            Template template = engine.getTemplate(templateContent, true);
            if (template.exists()) {
                template.render(engine, dataModel, out);
            }
        } catch (TemplateException e) {
            // 去掉某些堆栈信息
            IOUtils.writeQuietly(out, e.getMessage());
            throw BusinessException.create(e.getMessage(), e);
        }
    }

    @Override
    public void render(File template, Map<String, Object> dataModel, Writer out) {

    }

    /**
     * 获取模板渲染后的内容
     *
     * @param content   模板内容
     * @param dataModel 数据模型
     */
    @Override
    public String render(TemplateEngine engine, String content, Map<String, Object> dataModel) {
        if (CollectionUtils.isEmpty(dataModel)) {
            return content;
        }
        StringWriter sw = new StringWriter();
        try {
            renderTemplateString(engine, content, dataModel, sw);
        } catch (TemplateException exception) {
            throw new BusinessException("渲染模板失败，请检查模板语法", exception);
        }
        return content;
    }

    @Override
    public IPage<TemplateInfo> listPageTemplates(TemplateInfoListParam param) {
        LambdaQueryWrapper<TemplateInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(StringUtils.hasText(param.getTemplateType()), TemplateInfo::getProvider, param.getTemplateType());
        qw.like(StringUtils.hasText(param.getTemplateName()), TemplateInfo::getTemplateName, param.getTemplateName());
        return templateInfoMapper.selectPage(param, qw);
    }

    @Override
    public List<TemplateSelectVO> listSelectable() {
        return baseMapper.selectTemplateIdAndNames();
    }

    @Override
    public List<TemplateInfo> listInternalTemplates() {
        LambdaQueryWrapper<TemplateInfo> qw = new LambdaQueryWrapper<>();
        qw.eq(TemplateInfo::getInternal, true);
        return list(qw);
    }

    public Path getTemplateSaveLocation(String templateLocation, String dir) {
        if (templateLocation == null) {
            templateLocation = codeGenProperties.getTemplateLocation();
        }
        if (dir == null) {
            return Path.of(templateLocation, "devpl", codeGenProperties.getTemplateDirectory());
        }
        return Path.of(templateLocation, "devpl", codeGenProperties.getTemplateDirectory(), dir);
    }

    /**
     * 启动时进行模板迁移
     * 将 {@link CodeGenProperties#getTemplateDirectory()} 的目录下的所有模板文件移到 {@link CodeGenProperties#getTemplateLocation()} 目录下
     * 目录层级不变，同时更新模板信息
     */
    @Override
    public void migrateTemplates() {
        String templateLocation = codeGenProperties.getTemplateLocation();
        final Path targetLocation = getTemplateSaveLocation(templateLocation, null);
        if (!Files.exists(targetLocation)) {
            try {
                Files.createDirectories(targetLocation);
            } catch (IOException e) {
                log.error("模板迁移至{}失败", targetLocation, e);
                return;
            }
        } else {
            FileUtils.deleteDirectory(targetLocation);
        }
        File templateDir = ResourceUtils.getClassPathFile(codeGenProperties.getTemplateDirectory());
        if (templateDir == null || !templateDir.exists()) {
            log.error("模板迁移至{}失败，目录{}不存在", targetLocation, codeGenProperties.getTemplateDirectory());
            return;
        }
        try {
            log.info("复制模板 {} -> {}", templateDir, targetLocation.toFile());
            // 复制模板文件到本地文件目录
            if (!FileUtils.copyDirectories(templateDir, targetLocation)) {
                log.error("模板迁移至{}失败，复制文件失败", targetLocation);
            }

            updateTemplateOfDB(findTemplateFiles(targetLocation));
        } catch (Exception e) {
            log.error("模板迁移失败", e);
        }
    }

    /**
     * 比较存在的模板和数据库记录的模板信息
     *
     * @param templateFilePaths 所有模板文件
     */
    private void updateTemplateOfDB(List<Path> templateFilePaths) {
        // 库中已存在的模板
        List<TemplateInfo> internalTemplates = listInternalTemplates();
        final StringBuilder sb = new StringBuilder();
        if (!CollectionUtils.isEmpty(internalTemplates)) {
            // 绝对路径
            final Map<String, String> providerMap = TemplateEngineType.toExtensionProviderMap();
            // 待删除的模板
            final List<TemplateInfo> templateInfoToRemove = new ArrayList<>();
            // 项目目录下的模板信息 key-平台独立的文件绝对路径（数据库存储的字段） value
            Map<String, Path> templateFilePathMap = CollectionUtils.toMap(templateFilePaths, p -> formatTemplatePath(p.toAbsolutePath().toString()));
            final Iterator<TemplateInfo> iterator = internalTemplates.iterator();
            while (iterator.hasNext()) {
                TemplateInfo templateInfo = iterator.next();
                // 都是文件模板
                if (templateInfo.isFileTemplate() && !StringUtils.hasText(templateInfo.getTemplateFilePath())) {
                    iterator.remove();
                    templateInfoToRemove.add(templateInfo);
                }

                // 数据库中记录的模板在本地模板文件中不存在
                if (!templateFilePathMap.containsKey(templateInfo.getTemplateFilePath())) {
                    iterator.remove();
                    templateInfoToRemove.add(templateInfo);
                }
            }
            // 待更新的模板
            final List<TemplateInfo> templateInfoToUpdate = new ArrayList<>();
            // 待新增的模板
            final List<TemplateInfo> templateInfoToSave = new ArrayList<>();
            // 数据库的模板信息
            Map<String, TemplateInfo> pathToTemplateInfoMap = CollectionUtils.toMap(internalTemplates, TemplateInfo::getTemplateFilePath);
            for (Map.Entry<String, Path> entry : templateFilePathMap.entrySet()) {
                // 统一路径
                if (pathToTemplateInfoMap.containsKey(entry.getKey())) {
                    TemplateInfo templateInfo = pathToTemplateInfoMap.get(entry.getKey());
                    templateInfo = initTemplateInfo(entry.getValue(), providerMap, templateInfo);
                    if (templateInfo != null) {
                        templateInfoToUpdate.add(templateInfo);
                    }
                } else {
                    // 不存在，新增模板信息
                    TemplateInfo templateInfo = initTemplateInfo(entry.getValue(), providerMap, null);
                    if (templateInfo == null) {
                        continue;
                    }
                    templateInfoToSave.add(templateInfo);
                }
            }
            sb.append("更新").append(this.updateBatchById(templateInfoToUpdate) ? templateInfoToUpdate.size() : 0).append("个,");
            sb.append("删除").append(this.removeBatchByIds(templateInfoToRemove) ? templateInfoToRemove.size() : 0).append("个,");
            sb.append("保存").append(this.saveBatch(templateInfoToSave) ? templateInfoToSave.size() : 0).append("个");
        } else {
            List<TemplateInfo> templateInfos = initTemplateInfoList(templateFilePaths);
            sb.append("保存").append(this.saveBatch(templateInfos) ? templateInfos.size() : 0).append("个");
        }
        log.info("模板迁移结果: {}", sb);
    }

    public List<Path> findTemplateFiles(Path start) {
        List<Path> templateFilePaths = new ArrayList<>();
        try {
            Files.walkFileTree(start, new FileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    templateFilePaths.add(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
        return templateFilePaths;
    }

    @Nullable
    private TemplateInfo initTemplateInfo(Path templateFile, @Readonly Map<String, String> templateProviderMap, @Nullable TemplateInfo templateInfo) {
        String extName = FileUtils.getExtensionName(templateFile, null);
        if (!templateProviderMap.containsKey(extName)) {
            return null;
        }
        // 同一个路径表示唯一一个模板
        // 路径都是本地文件路径
        if (templateInfo == null) {
            templateInfo = new TemplateInfo();
        }
        templateInfo.setProvider(templateProviderMap.get(extName));
        templateInfo.setTemplateId(IdUtils.simpleULID());
        templateInfo.setInternal(true);
        templateInfo.setTemplateName(FileUtils.getFileName(templateFile));
        templateInfo.setDeleted(false);
        templateInfo.setTemplateFilePath(formatTemplatePath(templateFile.toAbsolutePath().toString()));
        templateInfo.setType(1);
        templateInfo.setUpdateTime(LocalDateTime.now());
        templateInfo.setRemark("");
        return templateInfo;
    }

    private List<TemplateInfo> initTemplateInfoList(List<Path> templateFilePaths) {
        List<TemplateInfo> templateInfos = new ArrayList<>();
        final Map<String, String> providerMap = TemplateEngineType.toExtensionProviderMap();
        for (Path templateFile : templateFilePaths) {
            TemplateInfo templateInfo = initTemplateInfo(templateFile, providerMap, null);
            if (templateInfo != null) {
                templateInfos.add(templateInfo);
            }
        }
        return templateInfos;
    }

    /**
     * 将路径全部统一用/进行分割
     * 统一不同操作系统的存储路径
     *
     * @param templatePath 模板路径
     * @return 用/进行分割的路径
     */
    @Override
    public String formatTemplatePath(String templatePath) {
        return templatePath.replace("\\", "/").replace("\\\\", "/");
    }

    /**
     * TODO 解析模板变量
     *
     * @param templateInfo 模板信息
     * @return 模板变量信息
     */
    @Override
    public List<TemplateVariableMetadata> introspect(TemplateInfo templateInfo) {
        return Collections.emptyList();
    }

    /**
     * 按 模板ID 查询模板 ID 和名称 Map
     *
     * @param templateIds 模板 ID
     * @return {@link Map}<{@link Long}, {@link String}>
     */
    @Override
    public Map<Long, String> listIdAndNameMapByIds(@Nullable Collection<Long> templateIds) {
        LambdaQueryWrapper<TemplateInfo> qw = new LambdaQueryWrapper<>();
        qw.select(TemplateInfo::getId, TemplateInfo::getTemplateName);
        qw.in(templateIds != null && !templateIds.isEmpty(), TemplateInfo::getId, templateIds);
        List<TemplateInfo> list = list(qw);
        if (list.isEmpty()) {
            return Collections.emptyMap();
        }
        HashMap<Long, String> map = new HashMap<>();
        for (TemplateInfo templateInfo : list) {
            map.put(templateInfo.getId(), templateInfo.getTemplateName());
        }
        return map;
    }

    @Override
    public TemplateInfo getTemplateInfoById(Long templateId) {
        TemplateInfo templateInfo = getById(templateId);
        if (templateInfo == null) {
            return null;
        }
        if (templateInfo.isFileTemplate() && StringUtils.hasText(templateInfo.getTemplateFilePath())) {
            templateInfo.setContent(FileUtils.readUTF8StringQuietly(new File(templateInfo.getTemplateFilePath())));
        }
        // 不暴露真实文件夹路径
        templateInfo.setTemplateFilePath("");
        return templateInfo;
    }

    @Override
    public List<TemplateParam> parseTemplateVariables(Long templateId) {


        return null;
    }
}
