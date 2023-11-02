package io.devpl.generator.controller;

import io.devpl.generator.common.query.Result;
import io.devpl.generator.domain.DataTypeVO;
import io.devpl.generator.domain.ParamNode;
import io.devpl.generator.domain.param.GetSqlParam;
import io.devpl.generator.domain.param.MyBatisParam;
import io.devpl.generator.enums.MapperStatementParamValueType;
import io.devpl.generator.mybatis.ParamMeta;
import io.devpl.generator.mybatis.ParseResult;
import io.devpl.generator.mybatis.tree.TreeNode;
import io.devpl.generator.service.MyBatisService;
import io.devpl.sdk.collection.Lists;
import io.devpl.sdk.io.FileUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

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
        ParseResult result = myBatisService.parseMapperStatement(content, param.getEnableTypeInference());
        // 根节点不使用
        TreeNode<ParamMeta> root = result.getRoot();
        final List<ParamNode> rows = new LinkedList<>();
        if (root.hasChildren()) {
            for (TreeNode<ParamMeta> node : root.getChildren()) {
                myBatisService.recursive(node, rows, -1);
            }
        }
        return Result.ok(rows);
    }

    /**
     * 获取Mapper Statement的参数值类型列表
     */
    @GetMapping("/ms/param/datatypes")
    public Result<List<DataTypeVO>> getDataTypes() throws Exception {
        List<DataTypeVO> list = Lists.arrayOf();
        MapperStatementParamValueType[] values = MapperStatementParamValueType.values();
        for (MapperStatementParamValueType type : values) {
            DataTypeVO dataTypeVO = new DataTypeVO();
            dataTypeVO.setName(type.name());
            dataTypeVO.setValue(type.name());
            dataTypeVO.setLabel(type.name());
            list.add(dataTypeVO);
        }
        return Result.ok(list);
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
