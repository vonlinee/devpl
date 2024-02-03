package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.devpl.backend.config.ThreadPoolConfiguration;
import io.devpl.backend.dao.CustomDirectiveMapper;
import io.devpl.backend.domain.param.CustomTemplateDirectiveParam;
import io.devpl.backend.entity.CustomDirective;
import io.devpl.backend.extension.compiler.CompilationResult;
import io.devpl.backend.extension.compiler.DynamicJavaCompiler;
import io.devpl.backend.service.CompilationService;
import io.devpl.backend.service.CrudService;
import io.devpl.backend.service.TemplateDirectiveService;
import io.devpl.codegen.template.TemplateDirective;
import io.devpl.codegen.template.TemplateEngine;
import io.devpl.sdk.util.ClassUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TemplateDirectiveServiceImpl implements TemplateDirectiveService {

    @Resource
    CustomDirectiveMapper customDirectiveMapper;
    @Resource
    CrudService crudService;
    @Resource
    DynamicJavaCompiler dynamicJavaCompiler;
    @Resource
    TemplateEngine templateEngine;
    @Resource
    CompilationService compilationService;
    @Resource
    @Qualifier(value = ThreadPoolConfiguration.COMMON_POOL)
    ThreadPoolTaskExecutor executor;

    AtomicInteger directiveNumber = new AtomicInteger(1);

    @Override
    public boolean addCustomDirective(CustomTemplateDirectiveParam param) {
        CustomDirective directive = new CustomDirective();
        directive.setDirectiveId(param.getDirectiveId());
        directive.setDirectiveName(param.getDirectiveName());
        directive.setSourceCode(param.getSourceCode());
        directive.setRemark(param.getRemark());
        directive.setStatus("");

        if (StringUtils.hasText(param.getSourceCode())) {

            // TODO

            String className = "CustomTemplateDirective" + directiveNumber.incrementAndGet();

            String packageName = "org.example.directive";

            String packageDeclaration = """
                package %s;

                import io.devpl.codegen.template.TemplateDirective;

                """.formatted(packageName);

            final String code = packageDeclaration + "\n" + param.getSourceCode().replace("CustomTemplateDirective", className);

            CompilationResult result = dynamicJavaCompiler.compile(packageName + "." + className, code);

            Class<?> clazz = result.getClass(className);

            Object instance = ClassUtils.instantiate(clazz);

            if (instance instanceof TemplateDirective td) {
                String directiveName = td.getName();

                System.out.println(directiveName);
            }
        }

        return crudService.saveOrUpdate(directive);
    }

    @Override
    public List<CustomDirective> listCustomDirectives(CustomTemplateDirectiveParam param) {
        return customDirectiveMapper.selectList(Wrappers.lambdaQuery());
    }

    @Override
    public boolean removeCustomDirective(CustomTemplateDirectiveParam param) {

        int affectedRows = customDirectiveMapper.deleteById(param.getDirectiveId());

        return false;
    }
}
