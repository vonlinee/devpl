package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.boot.CodeGenProperties;
import io.devpl.backend.common.exception.BusinessException;
import io.devpl.backend.dao.TemplateInfoMapper;
import io.devpl.backend.domain.TemplateEngineType;
import io.devpl.backend.domain.param.TemplateInfoListParam;
import io.devpl.backend.domain.vo.TemplateProviderVO;
import io.devpl.backend.domain.vo.TemplateSelectVO;
import io.devpl.backend.entity.TemplateInfo;
import io.devpl.backend.entity.TemplateVariableMetadata;
import io.devpl.backend.service.TemplateService;
import io.devpl.codegen.template.Template;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.codegen.template.TemplateException;
import io.devpl.codegen.template.beetl.BeetlTemplateEngine;
import io.devpl.codegen.template.enjoy.JFinalEnjoyTemplateEngine;
import io.devpl.codegen.template.freemarker.FreeMarkerTemplateEngine;
import io.devpl.codegen.template.velocity.VelocityTemplateEngine;
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.io.FilenameUtils;
import io.devpl.sdk.io.IOUtils;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.IdUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
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
            templateInfo.setTemplatePath(formatTemplatePath(file.getAbsolutePath()));
        }
        return templateInfoMapper.insert(templateInfo) == 1;
    }

    @Override
    public void render(Long templateId, Map<String, Object> dataModel, Writer out) {
        TemplateInfo templateInfo = getById(templateId);
        render(templateInfo, dataModel, out);
    }

    @Override
    public void render(@NotNull TemplateInfo templateInfo, Map<String, Object> dataModel, Writer out) throws TemplateException {
        if (StringUtils.hasText(templateInfo.getContent())) {
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
                Template template;
                if (templateInfo.isFileTemplate()) {
                    template = engine.getTemplate(templateInfo.getTemplatePath(), false);
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
        return templateInfoMapper.selectPage(param, new LambdaQueryWrapper<TemplateInfo>().eq(StringUtils.hasText(param.getTemplateType()), TemplateInfo::getProvider, param.getTemplateType()).like(StringUtils.hasText(param.getTemplateName()), TemplateInfo::getTemplateName, param.getTemplateName()));
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
     * 模板迁移
     * 启动时进行
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
        }
        URL codegenDir = this.getClass().getClassLoader().getResource(codeGenProperties.getTemplateDirectory());
        if (codegenDir != null) {
            File templateDir = new File(codegenDir.getPath());
            if (!templateDir.exists()) {
                log.error("模板迁移至{}失败，目录{}不存在", targetLocation, templateDir.getAbsolutePath());
                return;
            }

            if (!FileUtils.copyDirectories(templateDir, targetLocation.toFile())) {
                return;
            }
            log.info("复制模板 {} -> {}", templateDir, targetLocation.toFile());
            try {
                List<Path> templateFilePaths = new ArrayList<>();
                Path start = Files.walkFileTree(targetLocation, new FileVisitor<>() {
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

                List<TemplateInfo> internalTemplates = listInternalTemplates();
                Map<String, TemplateInfo> map = CollectionUtils.toMap(internalTemplates, TemplateInfo::getTemplatePath);

                LocalDateTime now = LocalDateTime.now();
                List<TemplateInfo> templateInfos = new ArrayList<>();
                for (Path templateFile : templateFilePaths) {
                    // 同一个路径表示唯一一个模板
                    // 路径都是本地文件路径
                    final String templatePath = formatTemplatePath(templateFile.toAbsolutePath().toString());
                    TemplateInfo templateInfo = map.get(templatePath);
                    if (templateInfo == null) {
                        templateInfo = new TemplateInfo();
                    }
                    String extName = FileUtils.getExtensionName(templateFile, null);
                    for (TemplateEngineType provider : TemplateEngineType.values()) {
                        if (provider.getExtension().equals(extName)) {
                            templateInfo.setProvider(provider.getProvider());
                            break;
                        }
                    }
                    templateInfo.setInternal(true);
                    templateInfo.setTemplateName(FileUtils.getFileName(templateFile));
                    templateInfo.setDeleted(false);
                    templateInfo.setTemplatePath(templatePath);
                    templateInfo.setType(1);
                    templateInfo.setUpdateTime(now);
                    templateInfo.setRemark("");

                    /**
                     * 如果是字符串模板，读取文件内容保存到数据库
                     */
                    if (templateInfo.isStringTemplate()) {
                        try {
                            templateInfo.setContent(FileUtils.readUTF8String(templateFile));
                        } catch (IOException e) {
                            log.error("读取文件失败 {} ", templateFile);
                        }
                    }
                    templateInfos.add(templateInfo);
                }
                saveOrUpdateBatch(templateInfos);
            } catch (IOException e) {
                log.error("模板迁移失败", e);
            }
        }
    }

    /**
     * 将路径全部统一用/进行分割
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
}
