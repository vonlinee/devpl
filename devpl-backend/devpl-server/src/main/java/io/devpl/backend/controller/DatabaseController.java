package io.devpl.backend.controller;

import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.entity.DataTypeItem;
import io.devpl.backend.service.DataTypeItemService;
import io.devpl.backend.service.DatabaseManageService;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数据库管理
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/dbm")
public class DatabaseController {

    @Resource
    DatabaseManageService databaseManageService;
    @Resource
    DataTypeItemService dataTypeItemService;

    /**
     * 表设计工具，获取每种数据库对应可选的数据类型
     *
     * @param dbType 数据库类型
     * @return 选项VO
     */
    @GetMapping("/design/table/column/types")
    public List<SelectOptionVO> getDataTypes(
        @NotBlank(message = "数据库类型不能为空") @RequestParam(name = "dbType") String dbType) {
        List<DataTypeItem> items = dataTypeItemService.listByGroupId(dbType);
        List<SelectOptionVO> result = new ArrayList<>();
        for (DataTypeItem item : items) {
            result.add(new SelectOptionVO(item.getTypeKey(), item.getTypeKey(),
                StringUtils.whenBlank(item.getFullTypeKey(), item.getTypeKey())));
        }
        return result;
    }

    /**
     * 根据列名称选择数据类型
     *
     * @param dbType 数据库类型
     * @return 选项VO
     */
    @GetMapping("/design/table/column/infer")
    public List<SelectOptionVO> inferColumnDataType(
        @NotBlank(message = "数据库类型不能为空") @RequestParam(name = "dbType") String dbType) {
        return Collections.emptyList();
    }
}
