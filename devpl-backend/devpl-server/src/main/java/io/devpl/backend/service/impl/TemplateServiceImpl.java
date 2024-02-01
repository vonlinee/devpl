package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import freemarker.template.Template;
import freemarker.template.TemplateException;
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
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
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

    /**p
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
        return templateInfoMapper.insert(templateInfo) == 1;
    }

    @Override
    public void render(Long templateId, Map<String, Object> dataModel, Writer out) {
        TemplateInfo templateInfo = getById(templateId);
        if (templateInfo == null) {
            log.error("模板不存在 {}", templateId);
            return;
        }
        if (StringUtils.hasText(templateInfo.getContent())) {
            render(templateInfo.getContent(), dataModel, out);
        }
    }

    @Override
    public void render(String content, Map<String, Object> dataModel, Writer out) {
        try (StringReader reader = new StringReader(content)) {
            // 渲染模板
            String templateName = dataModel.get("templateName").toString();
            Template template = new Template(templateName, reader, null, "utf-8");
            template.process(dataModel, out);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("渲染模板失败，请检查模板语法", e);
        } catch (TemplateException e) {
            throw new BusinessException("模板语法不正确", e);
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
    public String render(String content, Map<String, Object> dataModel) {
        if (CollectionUtils.isEmpty(dataModel)) {
            return content;
        }
        try (StringReader reader = new StringReader(content); StringWriter sw = new StringWriter()) {
            // 渲染模板
            String templateName = dataModel.get("templateName").toString();
            Template template = new Template(templateName, reader, null, "utf-8");
            template.process(dataModel, sw);
            content = sw.toString();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException("渲染模板失败，请检查模板语法", e);
        } catch (TemplateException e) {
            throw new BusinessException("模板语法不正确", e);
        }
        return content;
    }

    @Override
    public IPage<TemplateInfo> listPageTemplates(TemplateInfoListParam param) {
        return templateInfoMapper.selectPage(param.asPage(), new LambdaQueryWrapper<TemplateInfo>()
            .eq(StringUtils.hasText(param.getTemplateType()), TemplateInfo::getProvider, param.getTemplateType())
            .like(StringUtils.hasText(param.getTemplateName()), TemplateInfo::getTemplateName, param.getTemplateName()));
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

    /**
     * 模板迁移
     * 启动时进行
     */
    @Override
    public void migrateTemplates() {
        String templateLocation = codeGenProperties.getTemplateLocation();
        log.info("开始进行模板迁移");
        Path rootDir = Path.of(templateLocation, "devpl", codeGenProperties.getTemplateDirectory());
        if (!Files.exists(rootDir)) {
            try {
                Files.createDirectories(rootDir);
            } catch (IOException e) {
                log.error("模板迁移至{}失败", rootDir, e);
                return;
            }
        }
        URL codegenDir = this.getClass().getResource("/codegen");
        if (codegenDir != null) {
            File codegenFile = new File(codegenDir.getPath());
            Path path = codegenFile.toPath();

            Path templateLocationDir = Path.of(path.toString(), "templates");

            try {
                List<Path> templateFilePaths = new ArrayList<>();
                Path start = Files.walkFileTree(templateLocationDir, new FileVisitor<>() {
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


                Map<String, TemplateInfo> map = CollectionUtils.toMap(listInternalTemplates(), TemplateInfo::getTemplatePath);

                LocalDateTime now = LocalDateTime.now();
                List<TemplateInfo> templateInfos = new ArrayList<>();
                for (Path templateFile : templateFilePaths) {
                    final String templatePath = formatTemplatePath(FileUtils.getRelativePathString(templateFile, start));
                    // 同一个路径表示唯一一个模板
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
                    try {
                        templateInfo.setContent(FileUtils.readString(templateFile.toFile(), StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        log.error("读取文件失败 {} ", templateFile);
                    }

                    templateInfos.add(templateInfo);
                }
                saveOrUpdateBatch(templateInfos);
            } catch (IOException e) {
                log.error("模板迁移失败", e);
            }
        }
    }

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
