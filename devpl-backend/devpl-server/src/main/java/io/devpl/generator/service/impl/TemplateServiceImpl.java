package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.devpl.generator.boot.CodeGenProperties;
import io.devpl.generator.common.ServerException;
import io.devpl.generator.dao.TemplateInfoMapper;
import io.devpl.generator.domain.TemplateProvider;
import io.devpl.generator.domain.vo.TemplateSelectVO;
import io.devpl.generator.entity.TemplateInfo;
import io.devpl.generator.entity.TemplateVarInfo;
import io.devpl.generator.service.TemplateService;
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.util.CollectionUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        try (StringReader reader = new StringReader(content); StringWriter sw = new StringWriter();) {
            // 渲染模板
            String templateName = dataModel.get("templateName").toString();
            Template template = new Template(templateName, reader, null, "utf-8");
            template.process(dataModel, sw);
            content = sw.toString();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ServerException("渲染模板失败，请检查模板语法", e);
        } catch (TemplateException e) {
            throw new ServerException("", e);
        }
        return content;
    }

    @Override
    public IPage<TemplateInfo> listPageTemplates(int pageIndex, int pageSize) {
        return templateInfoMapper.selectPage(new Page<>(pageIndex, pageSize), new LambdaQueryWrapper<>());
    }

    @Override
    public List<TemplateSelectVO> listSelectable() {
        return baseMapper.selectTemplateIdAndNames();
    }

    /**
     * 模板迁移
     * 启动时进行
     *
     * @return 是否成功
     */
    @Override
    public boolean migrateTemplates() {
        String templateLocation = codeGenProperties.getTemplateLocation();

        Path rootDir = Path.of(templateLocation, "devpl", codeGenProperties.getTemplateDirectory());
        if (!Files.exists(rootDir)) {
            try {
                Files.createDirectories(rootDir);
            } catch (IOException e) {
                log.error("模板迁移至{}失败", rootDir, e);
                return false;
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
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        templateFilePaths.add(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }
                });

                LambdaQueryWrapper<TemplateInfo> qw = new LambdaQueryWrapper<>();
                qw.eq(TemplateInfo::getInternal, true);
                List<TemplateInfo> list = list(qw);

                Map<String, TemplateInfo> map = CollectionUtils.toMap(list, TemplateInfo::getTemplatePath);

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
                    for (TemplateProvider provider : TemplateProvider.values()) {
                        if (provider.getExtension().equals(extName)) {
                            templateInfo.setProvider(provider.getProviderName());
                            break;
                        }
                    }
                    templateInfo.setInternal(true);
                    templateInfo.setTemplateName(FileUtils.getFileName(templateFile));
                    templateInfo.setDeleted(false);
                    templateInfo.setTemplatePath(templatePath);
                    templateInfo.setType(1);
                    templateInfo.setCreateTime(now);
                    templateInfo.setUpdateTime(now);
                    templateInfo.setRemark(FileUtils.byteCountToDisplaySize(Files.size(templateFile), "%.2f"));
                    try {
                        templateInfo.setContent(FileUtils.readString(templateFile.toFile(), StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        log.error("读取文件失败 {} ", templateFile);
                    }

                    templateInfos.add(templateInfo);
                }

                saveOrUpdateBatch(templateInfos);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    @Override
    public String formatTemplatePath(String templatePath) {
        return templatePath.replace("\\", "/").replace("\\\\", "/");
    }

    @Override
    public List<TemplateVarInfo> introspect(TemplateInfo templateInfo) {
        return null;
    }
}
