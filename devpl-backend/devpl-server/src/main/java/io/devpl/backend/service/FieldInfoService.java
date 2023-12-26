package io.devpl.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.backend.common.mvc.BaseService;
import io.devpl.backend.domain.param.FieldInfoListParam;
import io.devpl.backend.domain.param.FieldParseParam;
import io.devpl.backend.entity.FieldGroup;
import io.devpl.backend.entity.FieldInfo;

import java.util.List;

/**
 * 字段信息 Service
 */
public interface FieldInfoService extends BaseService<FieldInfo> {

    List<FieldInfo> listFields(FieldInfoListParam param);

    IPage<FieldInfo> selectPage(FieldInfoListParam param);

    List<FieldInfo> parseFields(FieldParseParam param);

    boolean saveFieldsInfos(List<FieldInfo> fieldInfo);

    List<String> listFieldKeys();

    boolean addFieldGroup(List<FieldGroup> groups);

    /**
     * 获取示例文本
     *
     * @param type 输入类型
     * @return 示例文本
     */
    String getSampleText(String type);
}
