package io.devpl.generator.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.generator.common.service.BaseService;
import io.devpl.generator.entity.FieldInfo;

public interface FieldInfoService extends BaseService<FieldInfo> {

    IPage<FieldInfo> pages(int pageIndex, int pageSize);
}
