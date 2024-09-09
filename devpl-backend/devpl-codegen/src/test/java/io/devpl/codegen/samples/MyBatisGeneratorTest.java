package io.devpl.codegen.samples;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.StringUtility;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyBatisGeneratorTest {

    @Test
    public void test1() {
        String config = MyBatisGeneratorTest.class.getClassLoader().getResource("generatorConfig.xml").getFile();
        String[] arg = {"-configfile", config, "-overwrite"};
        ShellRunner.main(arg);
    }

    @Test
    public void test2() throws XMLParserException, IOException, InvalidConfigurationException, SQLException, InterruptedException {
        List<String> warnings = new ArrayList<>();
        String configFile = MyBatisGeneratorTest.class.getClassLoader().getResource("generatorConfig.xml").getFile();
        File configurationFile = new File(configFile);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configurationFile);
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);

        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);

        ProgressCallback progressCallback = new VerboseProgressCallback();
        Set<String> contextIds = StringUtility.tokenize("");
        Set<String> fullyQualifiedTables = new HashSet<>();
        myBatisGenerator.generate(progressCallback, contextIds, fullyQualifiedTables);

        List<Context> contexts = config.getContexts();
        for (Context context : contexts) {
            context.introspectTables(progressCallback, new ArrayList<>(), new HashSet<>());
        }
    }
}
