package io.devpl.codegen.samples;

import io.devpl.codegen.generator.CodeGenerator;
import io.devpl.codegen.generator.config.ConfigurationParser;
import io.devpl.codegen.generator.RdbmsTableGenerationContext;
import io.devpl.codegen.generator.config.Configuration;
import io.devpl.codegen.generator.config.JdbcConfiguration;
import io.devpl.codegen.generator.config.xml.XMLParserException;
import io.devpl.codegen.util.Utils;
import org.junit.Test;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 如果 src/main/ 下也有模板文件，优先使用 src/test 下的资源文件
 */
public class MySQLGeneratorTest {

    @Test
    public void test1() throws XMLParserException, IOException {
        List<String> warnings = new ArrayList<>();
        String configFile = MyBatisGeneratorTest.class.getClassLoader().getResource("config.xml").getFile();
        File configurationFile = new File(configFile);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configurationFile);
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);
    }

    /**
     * @see org.mybatis.generator.config.Context
     */
    @Test
    public void testPrivateGenerateFiles() {
        File file = new File(Utils.getDesktopDirectory(), "codegen.properties");
        Properties properties = Utils.loadProperties(file);
        JdbcConfiguration jdbcConfiguration = JdbcConfiguration.builder(properties).build();

        // 配置信息
        Configuration configuration = new Configuration();
        RdbmsTableGenerationContext context = new RdbmsTableGenerationContext(jdbcConfiguration);
        context.addTableConfiguration("table_file_generation");

        configuration.addContext(context);

        CodeGenerator generator = new CodeGenerator(configuration);
        // 生成文件
        generator.generateFiles(null);
    }
}
