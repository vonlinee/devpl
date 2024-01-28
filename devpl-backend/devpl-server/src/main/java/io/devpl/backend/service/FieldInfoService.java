package io.devpl.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.backend.common.mvc.BaseService;
import io.devpl.backend.domain.param.FieldInfoListParam;
import io.devpl.backend.domain.param.FieldParseParam;
import io.devpl.backend.domain.vo.FieldParseResult;
import io.devpl.backend.entity.FieldGroup;
import io.devpl.backend.entity.FieldInfo;
import io.devpl.common.exception.FieldParseException;

import java.util.Collection;
import java.util.List;

/**
 * 字段信息 Service
 */
public interface FieldInfoService extends BaseService<FieldInfo> {

    List<FieldInfo> listFields(FieldInfoListParam param);

    IPage<FieldInfo> selectPage(FieldInfoListParam param);

    FieldParseResult parseFields(FieldParseParam param) throws FieldParseException;

    boolean saveFieldsInfos(List<FieldInfo> fieldInfo, boolean allowFieldKeyDuplicated);

    List<String> listFieldKeys();

    /**
     * 前端组件使用id字段维持唯一性
     *
     * @param fields 字段列表
     */
    void batchSetFieldValue(Collection<FieldInfo> fields, boolean setIdToNull, boolean temporary, boolean deleted);

    boolean addFieldGroup(List<FieldGroup> groups);

    /**
     * 获取示例文本
     *
     * @param type 输入类型
     * @return 示例文本
     */
    String getSampleText(String type);
}
