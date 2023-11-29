package io.devpl.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.backend.common.mvc.BaseService;
import io.devpl.backend.domain.param.FieldInfoListParam;
import io.devpl.backend.domain.param.FieldParseParam;
import io.devpl.backend.entity.FieldInfo;

import java.util.List;

/**
 * 字段信息 Service
 */
public interface FieldInfoService extends BaseService<FieldInfo> {

    IPage<FieldInfo> selectPage(FieldInfoListParam param);

    List<FieldInfo> parseFields(FieldParseParam param);
}
