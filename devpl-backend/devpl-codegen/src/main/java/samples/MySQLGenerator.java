package samples;

import io.devpl.codegen.core.FastAutoGenerator;
import io.devpl.codegen.util.InternalUtils;

import java.io.File;

public class MySQLGenerator {

    public static void main(String[] args) {
        FastAutoGenerator
            // 配置数据源
            .create(new File(InternalUtils.getDesktopDirectory(), "jdbc.properties"))
            // 全局配置
            .globalConfig(builder -> {
                builder.author("author") // 设置作者
                    .commentDatePattern("yyyy-MM-dd hh:mm:ss")   // 注释日期
                    .outputDir("E://Temp"); // 指定输出目录
            }).strategyConfig(builder -> {
                builder.addInclude("data_type_group");
                builder.entityBuilder().enableFileOverride();
                builder.serviceBuilder().enableFileOverride();
                builder.mapperBuilder().enableFileOverride();
                builder.controllerBuilder().enableFileOverride();
            }).execute();
    }
}
