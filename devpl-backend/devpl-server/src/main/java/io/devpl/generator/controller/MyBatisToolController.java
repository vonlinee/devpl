package io.devpl.generator.controller;

import io.devpl.generator.common.query.Result;
import io.devpl.generator.domain.DataTypeVO;
import io.devpl.generator.domain.ParamNode;
import io.devpl.generator.domain.param.GetSqlParam;
import io.devpl.generator.domain.param.MyBatisParam;
import io.devpl.generator.enums.MapperStatementParamValueType;
import io.devpl.generator.mybatis.ParseResult;
import io.devpl.generator.mybatis.tree.TreeNode;
import io.devpl.generator.service.MyBatisService;
import io.devpl.sdk.collection.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

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
     * 获取Mapper Statemtn的参数列表
     * JSON传参
     */
    @PostMapping("/ms/params")
    public Result<List<ParamNode>> getMapperStatementParams(@RequestBody MyBatisParam param) throws Exception {
        String content = param.getMapperStatement();
        Assert.hasText(content, "文本为空");
        ParseResult result = myBatisService.paraseMapperStatement(content);
        // 根节点不使用
        TreeNode<String> root = result.getRoot();
        final List<ParamNode> rows = new LinkedList<>();
        if (root.hasChildren()) {
            for (TreeNode<String> node : root.getChildren()) {
                myBatisService.recursive(node, rows, -1);
            }
        }
        return Result.ok(rows);
    }

    /**
     * 获取Mapper Statemtn的参数值类型列表
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
     * 获取Mapper Statemtn结合输入参数获取sql
     */
    @PostMapping("/ms/sql")
    public Result<String> getPreCompliedSql(@RequestBody GetSqlParam param) {
        return Result.ok(myBatisService.getPreCompliedSql(param));
    }
}
