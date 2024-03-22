package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.entity.DataTypeItem;
import io.devpl.backend.entity.DataTypeMapping;

import java.util.List;

public interface DataTypeMappingService extends IService<DataTypeMapping> {
    List<SelectOptionVO> listMappingGroupOptions();

    List<SelectOptionVO> listSelectablePrimaryTypeOptions();

    List<SelectOptionVO> listSelectableAnotherTypeOptions(DataTypeItem typeItem);
}
