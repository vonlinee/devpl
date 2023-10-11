package io.devpl.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.generator.common.mvc.BaseService;
import io.devpl.generator.entity.FieldInfo;

/**
 * 字段信息 Service
 */
public interface FieldInfoService extends BaseService<FieldInfo> {

    IPage<FieldInfo> pages(int pageIndex, int pageSize);
}
