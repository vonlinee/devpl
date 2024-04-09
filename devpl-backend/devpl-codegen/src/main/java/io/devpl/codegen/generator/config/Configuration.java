package io.devpl.codegen.generator.config;

import io.devpl.codegen.generator.Context;
import io.devpl.codegen.generator.file.TargetFile;
import io.devpl.codegen.util.Messages;
import io.devpl.codegen.util.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Configuration extends PropertyHolder implements PropertyObject {

    private List<Context> contexts;
    /**
     * 项目配置信息
     */
    private ProjectConfiguration projectConfiguration;

    /**
     * 需要通过反射创建的类，全限定类名
     */
    private final List<String> classPathEntries;
    private final List<TargetFile> targetFiles;

    /**
     * 是否在生成文件之前先清空根目录
     * 注意，不要设置为根目录，防止清空不相关的文件夹
     */
    private boolean clearBeforeWriteFiles = true;

    public Configuration() {
        super();
        contexts = new ArrayList<>();
        classPathEntries = new ArrayList<>();
        targetFiles = new ArrayList<>();
        targetFiles.addAll(List.of(BuiltinTargetFile.values()));
    }

    public void addClasspathEntry(String entry) {
        classPathEntries.add(entry);
    }

    public List<String> getClassPathEntries() {
        return classPathEntries;
    }

    /**
     * This method does a simple validate, it makes sure that all required fields have been filled in and that all
     * implementation classes exist and are of the proper type. It does not do any more complex operations such as:
     * validating that database tables exist or validating that named columns exist
     */
    public void validate() throws RuntimeException {
        List<String> errors = new ArrayList<>();
        for (String classPathEntry : classPathEntries) {
            if (!StringUtils.hasText(classPathEntry)) {
                errors.add(Messages.getString("ValidationError.19"));
                // only need to state this error once
                break;
            }
        }
        if (contexts.isEmpty()) {
            errors.add(Messages.getString("ValidationError.11"));
        } else {
            for (Context context : contexts) {
                context.putObject(this.projectConfiguration);
                context.validate(errors);
            }
        }
        if (!errors.isEmpty()) {
            throw new RuntimeException("");
        }
    }

    public final List<Context> getContexts() {
        return contexts;
    }

    public final void addContext(Context context) {
        if (contexts == null) {
            contexts = new ArrayList<>();
        }
        for (TargetFile targetFile : targetFiles) {
            context.registerTargetFile(targetFile);
        }
        this.contexts.add(context);
    }
}
