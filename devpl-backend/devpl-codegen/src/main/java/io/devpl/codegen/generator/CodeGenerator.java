package io.devpl.codegen.generator;

import io.devpl.codegen.generator.config.Configuration;
import io.devpl.codegen.generator.config.GlobalConfiguration;
import io.devpl.codegen.generator.config.PropertyHolder;
import io.devpl.sdk.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CodeGenerator extends PropertyHolder {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 代码生成器配置
     */
    private Configuration configuration;

    public CodeGenerator(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 生成文件
     *
     * @param callback 进度回调
     */
    public final void generateFiles(ProgressCallback callback) {
        List<Context> contextsToRun = configuration.getContexts();
        if (contextsToRun == null || contextsToRun.isEmpty()) {
            throw new RuntimeException("no context to run");
        }
        if (callback == null) {
            callback = new ProgressCallback.NoOp();
        }
        for (Context context : contextsToRun) {
            callback.startTask("initialize context");
            context.initialize();
        }
        for (Context context : contextsToRun) {
            List<GeneratedFile> generatedFiles = new ArrayList<>();
            log.info("Context {} 开始生成文件", context.getId());
            List<String> errors = new ArrayList<>();
            try {
                context.generateFiles(callback, generatedFiles, errors);
            } catch (InterruptedException exception) {
                // terminate the file generation
                callback.terminated();
                return;
            }
            if (configuration.isClearBeforeWriteFiles()) {
                // 清空文件夹
                GlobalConfiguration config = context.getObject(GlobalConfiguration.class);
                String outputDir = config.getOutputDir();
                FileUtils.clean(new File(outputDir));
            }
            writeFiles(generatedFiles);
        }
    }

    // 写入文件
    public void writeFiles(List<GeneratedFile> files) {
        for (GeneratedFile file : files) {
            try {
                file.write(null, StandardCharsets.UTF_8);
            } catch (IOException e) {
                log.error("error when write file {}", file.getFilename());
            }
        }
    }
}
