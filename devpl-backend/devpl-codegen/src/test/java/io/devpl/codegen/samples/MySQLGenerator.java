package io.devpl.codegen.samples;

import io.devpl.codegen.core.AutoGenerator;
import io.devpl.codegen.core.FastAutoGenerator;
import io.devpl.codegen.samples.ui.GenerationResultView;
import io.devpl.codegen.samples.ui.UIHelper;
import io.devpl.codegen.util.CodeGeneratorUtils;

import java.io.File;

public class MySQLGenerator {

    public static void main(String[] args) {
        AutoGenerator generator = FastAutoGenerator
            // 配置数据源
            .create(new File(CodeGeneratorUtils.getDesktopDirectory(), "codegen.properties"))
            // 全局配置
            .globalConfig(builder -> {
                builder.author("author") // 设置作者
                    .commentDatePattern("yyyy-MM-dd hh:mm:ss")   // 注释日期
                    .outputDir("D://Temp//codegen"); // 指定输出目录
            }).strategyConfig(builder -> {
                builder.addInclude("data_type_group");
                builder.entityBuilder().enableFileOverride();
                builder.serviceBuilder().enableFileOverride();
                builder.mapperBuilder().enableFileOverride();
                builder.controllerBuilder().enableFileOverride();
            })
            .packageConfig(builder -> {
                builder.controller("");
            })
            .execute();

        // generator.show(rootDir -> UIHelper.showFrame("生成结果", new GenerationResultView(new File(rootDir)), 800, 600));
    }
}
