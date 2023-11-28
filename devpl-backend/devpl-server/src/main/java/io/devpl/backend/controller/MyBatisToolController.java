package io.devpl.backend.controller;

import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.DataTypeVO;
import io.devpl.backend.domain.ParamNode;
import io.devpl.backend.domain.param.GetSqlParam;
import io.devpl.backend.domain.param.MyBatisParam;
import io.devpl.backend.domain.enums.MapperStatementParamValueType;
import io.devpl.backend.mybatis.ParamMeta;
import io.devpl.backend.service.MyBatisService;
import io.devpl.backend.utils.Vals;
import io.devpl.sdk.io.FileUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MyBatis 控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/tools/mybatis")
public class MyBatisToolController {

    @Resource
    private MyBatisService myBatisService;

    /**
     * 获取样例XML Statement
     */
    @GetMapping("/ms/sample")
    public Result<String> getSampleXml() {
        URL resource = getClass().getResource("/static/samples/sample.ms.xml");
        if (resource == null) {
            return Result.error("获取示例失败");
        }
        File file = new File(resource.getFile());
        try {
            return Result.ok(FileUtils.readString(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Mapper Statement的参数列表
     * JSON传参
     */
    @PostMapping("/ms/params")
    public Result<List<ParamNode>> getMapperStatementParams(@RequestBody MyBatisParam param) throws Exception {
        String content = param.getMapperStatement();
        Assert.hasText(content, "文本为空");
        return Result.ok(myBatisService.getMapperStatementParams(content, Vals.nil(param.getEnableTypeInference(), true)));
    }

    /**
     * 获取Mapper Statement的参数值类型列表
     */
    @GetMapping("/ms/param/datatypes")
    public Result<List<DataTypeVO>> getDataTypes() throws Exception {
        return Result.ok(Arrays.stream(MapperStatementParamValueType.values()).map(i -> new DataTypeVO(i.name())).collect(Collectors.toList()));
    }

    /**
     * 获取Mapper Statement结合输入参数获取sql
     */
    @PostMapping("/ms/sql")
    public Result<String> getSqlOfMappedStatement(@RequestBody GetSqlParam param) {
        return Result.ok(myBatisService.getSqlOfMappedStatement(param));
    }

    /**
     * 获取Mapper Statement结合输入参数获取参数元数据
     */
    @PostMapping("/ms/param/meta")
    public Result<List<ParamMeta>> getParamMetaData(@RequestBody GetSqlParam param) {
        return Result.ok(myBatisService.getParamMetadata(param.getMapperStatement()));
    }
}
