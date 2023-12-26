package io.devpl.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.devpl.backend.domain.param.FieldGroupListParam;
import io.devpl.backend.entity.FieldGroup;

import java.util.List;

public interface FieldGroupService {

    List<FieldGroup> listFieldGroups(FieldGroupListParam param);

    IPage<FieldGroup> listPage(FieldGroupListParam param);
}
