package samples;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

public class MySQLGenerator {

    public static void main(String[] args) {
        FastAutoGenerator
            // 配置数据源
            .create("jdbc:mysql://127.0.0.1:3306/devpl?characterEncoding=UTF-8&useUnicode=true&useSSL=false", "root", "123456")
            // 全局配置
            .globalConfig(builder -> {
                builder.author("wanggc") // 设置作者
                    .commentDatePattern("yyyy-MM-dd hh:mm:ss")   // 注释日期
                    .outputDir("D:/Temp"); // 指定输出目录
            }).strategyConfig(builder -> {
                builder.addInclude("dbs");
                builder.entityBuilder().enableFileOverride();
                builder.serviceBuilder().enableFileOverride();
                builder.mapperBuilder().enableFileOverride();
                builder.controllerBuilder().enableFileOverride();
            }).templateEngine(new VelocityTemplateEngine()).execute();
    }
}
