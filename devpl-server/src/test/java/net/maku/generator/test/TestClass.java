package net.maku.generator.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.devpl.generator.dao.DistrictMapper;
import io.devpl.generator.entity.District;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackages = "net.maku.generator")
@MapperScan(basePackages = {"net.maku.generator.dao"})
public class TestClass {

    @Autowired
    private DistrictMapper districtMapper;

    @Test
    public void testAddUser() {
        List<District> districts = districtMapper.selectList(new LambdaQueryWrapper<>());
        System.out.println(districts);
    }
}
