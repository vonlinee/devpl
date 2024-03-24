package io.devpl.codegen.samples;

import io.devpl.codegen.core.AutoGenerator;
import io.devpl.codegen.core.FastAutoGenerator;
import io.devpl.codegen.util.InternalUtils;

import java.io.File;

public class MySQLGenerator {

    public static void main(String[] args) {
        AutoGenerator generator = FastAutoGenerator
            // 配置数据源
            .create(new File(InternalUtils.getDesktopDirectory(), "codegen.properties"))
            // 全局配置
            .globalConfig(builder -> {
                builder.author("author") // 设置作者
                    .commentDatePattern("yyyy-MM-dd hh:mm:ss")   // 注释日期
                    .outputDir("D://Temp//codegen"); // 指定输出目录
            }).strategyConfig(builder -> {
                builder.addInclude("template_info");
//                builder.addInclude("graduation_resit_exam_application");
//                builder.addInclude("graduation_resit_exam_review");
                builder.entityBuilder().enableFileOverride();
                builder.serviceBuilder().enableFileOverride();
                builder.mapperBuilder().enableFileOverride();
                builder.controllerBuilder().enableFileOverride();
            })
//            .templateConfig(builder -> {
//                builder.controller("")
//                    .entity("")
//                    .service("");
//            })
            .packageConfig(builder -> builder.parent("com.lancoo.examuniv"))
            .execute();

        generator.open();
    }
}
