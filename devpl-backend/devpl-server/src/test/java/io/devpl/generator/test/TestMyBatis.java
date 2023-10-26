package io.devpl.generator.test;

import io.devpl.generator.mybatis.ParamMeta;
import io.devpl.generator.service.MyBatisService;
import io.devpl.sdk.io.FileUtils;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TestMyBatis {

    @Resource
    MyBatisService myBatisService;

    @Test
    public void test1() throws IOException {

        String absolutePath = new File("").getAbsolutePath();

        File file = new File(new File(absolutePath).getParent(), "sample/mapper.xml.txt");

        System.out.println(myBatisService);

        String xml = FileUtils.readString(file);

        List<ParamMeta> paramMetaData = myBatisService.getParamMetadata(xml);
    }
}
