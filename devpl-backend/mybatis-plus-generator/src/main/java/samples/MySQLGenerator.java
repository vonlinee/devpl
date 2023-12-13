package samples;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.velocity.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.util.InternalUtils;

import java.io.File;

public class MySQLGenerator {

    public static void main(String[] args) {
        FastAutoGenerator
            // 配置数据源
            .create(new File(InternalUtils.getDesktop(), "jdbc.properties"))
            // 全局配置
            .globalConfig(builder -> {
                builder.author("author") // 设置作者
                    .commentDatePattern("yyyy-MM-dd hh:mm:ss")   // 注释日期
                    .outputDir("/Temp"); // 指定输出目录
            }).strategyConfig(builder -> {
                builder.addInclude("");
                builder.entityBuilder().enableFileOverride();
                builder.serviceBuilder().enableFileOverride();
                builder.mapperBuilder().enableFileOverride();
                builder.controllerBuilder().enableFileOverride();
            }).templateEngine(new VelocityTemplateEngine()).execute();
    }
}
