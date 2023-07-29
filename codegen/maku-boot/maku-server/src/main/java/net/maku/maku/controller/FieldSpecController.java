package net.maku.maku.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.common.utils.Result;
import net.maku.maku.convert.FieldSpecConvert;
import net.maku.maku.entity.FieldSpecEntity;
import net.maku.maku.service.FieldSpecService;
import net.maku.maku.query.FieldSpecQuery;
import net.maku.maku.vo.FieldSpecVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
* 字段信息表
*
* @author 111 222
* @since 1.0.0 2023-07-29
*/
@RestController
@RequestMapping("maku/spec")
@Tag(name="字段信息表")
@AllArgsConstructor
public class FieldSpecController {
private final FieldSpecService fieldSpecService;

@GetMapping("page")
@Operation(summary = "分页")
@PreAuthorize("hasAuthority('maku:spec:page')")
public Result
<PageResult
<FieldSpecVO>> page(@ParameterObject @Valid FieldSpecQuery query){
    PageResult
    <FieldSpecVO> page = fieldSpecService.page(query);

        return Result.ok(page);
        }

        @GetMapping("{id}")
        @Operation(summary = "信息")
        @PreAuthorize("hasAuthority('maku:spec:info')")
        public Result
        <FieldSpecVO> get(@PathVariable("id") Long id){
            FieldSpecEntity entity = fieldSpecService.getById(id);

            return Result.ok(FieldSpecConvert.INSTANCE.convert(entity));
            }

            @PostMapping
            @Operation(summary = "保存")
            @PreAuthorize("hasAuthority('maku:spec:save')")
            public Result
            <String> save(@RequestBody FieldSpecVO vo){
                fieldSpecService.save(vo);

                return Result.ok();
                }

                @PutMapping
                @Operation(summary = "修改")
                @PreAuthorize("hasAuthority('maku:spec:update')")
                public Result
                <String> update(@RequestBody @Valid FieldSpecVO vo){
                    fieldSpecService.update(vo);

                    return Result.ok();
                    }

                    @DeleteMapping
                    @Operation(summary = "删除")
                    @PreAuthorize("hasAuthority('maku:spec:delete')")
                    public Result
                    <String> delete(@RequestBody List
                        <Long> idList){
                            fieldSpecService.delete(idList);

                            return Result.ok();
                            }
                            }
