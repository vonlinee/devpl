package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.TemplateParamMapper;
import io.devpl.backend.entity.TemplateParam;
import io.devpl.backend.service.TemplateParamService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模板参数Service
 */
@Service
public class TemplateParamServiceImpl extends ServiceImpl<TemplateParamMapper, TemplateParam> implements TemplateParamService {

    /**
     * 获取全局模板参数
     * 模板ID为NULL的为全局参数
     *
     * @return 全局模板参数
     */
    @Override
    public List<TemplateParam> getGlobalTemplateParams() {
        LambdaQueryWrapper<TemplateParam> qw = new LambdaQueryWrapper<>();
        qw.eq(TemplateParam::getTemplateId, null);
        return list(qw);
    }
}
