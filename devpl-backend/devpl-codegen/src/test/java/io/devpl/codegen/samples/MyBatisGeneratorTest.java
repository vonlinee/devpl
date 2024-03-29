package io.devpl.codegen.samples;

import org.junit.Test;
import org.mybatis.generator.api.ShellRunner;

public class MyBatisGeneratorTest {

    @Test
    public void test1() {
        String config = MyBatisGeneratorTest.class.getClassLoader().getResource("generatorConfig.xml").getFile();
        String[] arg = {"-configfile", config, "-overwrite"};
        ShellRunner.main(arg);
    }
}
