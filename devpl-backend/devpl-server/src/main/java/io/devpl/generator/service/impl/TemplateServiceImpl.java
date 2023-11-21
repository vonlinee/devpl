package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.devpl.codegen.utils.CollectionUtils;
import io.devpl.generator.common.ServerException;
import io.devpl.generator.dao.TemplateInfoMapper;
import io.devpl.generator.domain.vo.TemplateSelectVO;
import io.devpl.generator.entity.TemplateInfo;
import io.devpl.generator.service.TemplateService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
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

    /**
     * 保存模板
     *
     * @param templateInfo 模板信息
     * @return 是否成功
     */
    @Override
    public boolean save(TemplateInfo templateInfo) {
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
}
