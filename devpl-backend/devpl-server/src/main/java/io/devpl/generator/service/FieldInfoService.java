package io.devpl.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.generator.common.mvc.BaseService;
import io.devpl.generator.domain.param.FieldParseParam;
import io.devpl.generator.entity.FieldInfo;

import java.util.List;

/**
 * 字段信息 Service
 */
public interface FieldInfoService extends BaseService<FieldInfo> {

    IPage<FieldInfo> pages(int pageIndex, int pageSize);

    List<FieldInfo> parseFields(FieldParseParam param);
}
