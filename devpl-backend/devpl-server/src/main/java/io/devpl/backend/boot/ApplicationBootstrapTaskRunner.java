package io.devpl.backend.boot;

import io.devpl.backend.service.TemplateService;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 后台启动时执行
 */
@Slf4j
@Component
public class ApplicationBootstrapTaskRunner implements CommandLineRunner {

    @Resource
    TemplateService templateService;

    @Resource
    @Qualifier(value = "common-pool")
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void run(String... args) throws Exception {
        // 复制模板到本地文件系统
        CompletableFuture.runAsync(() -> templateService.migrateTemplates(), threadPoolTaskExecutor);
    }
}
