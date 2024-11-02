package io.devpl.backend.controller;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.common.query.Result;
import io.devpl.backend.domain.MsParamNode;
import io.devpl.backend.tools.mybatis.MSParamDataType;
import io.devpl.backend.domain.param.GetSqlParam;
import io.devpl.backend.domain.param.MappedStatementListParam;
import io.devpl.backend.domain.param.MyBatisParam;
import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.entity.MappedStatementItem;
import io.devpl.backend.service.MyBatisService;
import io.devpl.sdk.io.FileUtils;
import io.devpl.sdk.util.ArrayUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URL;
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
        return Result.ok(FileUtils.readStringQuietly(new File(resource.getFile())));
    }

    /**
     * 获取Mapper Statement的所有参数名及推断参数类型
     * 树形结构
     */
    @PostMapping("/ms/params")
    public Result<List<MsParamNode>> getMapperStatementParams(@RequestBody MyBatisParam param) {
        String content = param.getMapperStatement();
        if (StringUtils.isBlank(content)) {
            return Result.error("文本为空");
        }
        return Result.ok(myBatisService.getMapperStatementParams(content, param.isTypeInferEnabled()));
    }

    /**
     * 获取Mapper Statement的可选的参数值类型枚举
     */
    @GetMapping("/ms/param/datatype/list")
    public Result<List<SelectOptionVO>> getDataTypes() {
        return Result.ok(ArrayUtils.toList(MSParamDataType.values(), i -> new SelectOptionVO(i.name(), i.getQualifier(), i.getQualifier())));
    }

    /**
     * 获取Mapper Statement结合输入参数获取sql
     *
     * @param param 参数
     * @return {@link Result}<{@link String}>
     */
    @PostMapping("/ms/sql")
    public String getSqlOfMappedStatement(@RequestBody GetSqlParam param) {
        return myBatisService.getSqlOfMappedStatement(param);
    }

    /**
     * 获取Mapper Statement结合输入参数获取参数元数据
     */
    @GetMapping("/index/build")
    public Result<List<String>> getParamMetaData(String projectRootDir) {
        return Result.ok(myBatisService.buildIndex(projectRootDir));
    }

    /**
     * 解析并保存Mapped Statement信息
     */
    @GetMapping("/index/build/ms")
    public void buildMappedStatementIndex(@RequestParam(value = "dir") String projectRootDir, @RequestParam(value = "reset") Boolean reset) {
        myBatisService.buildMapperXmlIndexForProject(projectRootDir, reset != null && reset);
    }

    /**
     * 解析并保存Mapped Statement信息
     */
    @GetMapping("/index/project")
    public Result<List<String>> listIndexedProjectRootPaths() {
        return Result.ok(myBatisService.listIndexedProjectRootPaths());
    }

    /**
     * 获取Mapper Statement结合输入参数获取参数元数据
     */
    @GetMapping("/index/query/ms")
    public Result<String> getParamMetaData(String projectRootDir, String mapperStatementId) {
        return Result.ok(myBatisService.getContent(projectRootDir, mapperStatementId));
    }

    /**
     * 查询解析了的Mapper Statement列表
     */
    @GetMapping("/index/query/mslist")
    public ListResult<MappedStatementItem> listIndexedMappedStatements(MappedStatementListParam param) {
        return ListResult.ok(myBatisService.pageIndexedMappedStatements(param));
    }
}
