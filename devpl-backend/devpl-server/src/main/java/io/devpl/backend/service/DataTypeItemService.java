package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.domain.param.DataTypeListParam;
import io.devpl.backend.domain.param.DataTypeMappingParam;
import io.devpl.backend.domain.vo.DataTypeGroupVO;
import io.devpl.backend.domain.vo.DataTypeMappingListVO;
import io.devpl.backend.domain.vo.DataTypeMappingVO;
import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.entity.DataTypeGroup;
import io.devpl.backend.entity.DataTypeItem;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;

public interface DataTypeItemService extends IService<DataTypeItem> {

    boolean saveDataTypes(Collection<DataTypeItem> dataTypeItems);

    boolean save(DataTypeItem dataTypeItem);

    boolean update(DataTypeItem dataTypeItem);

    boolean removeById(Long typeId);

    boolean saveOrUpdateTypeGroups(List<DataTypeGroup> dataTypeGroups);

    boolean saveDataTypeGroup(DataTypeGroup typeGroup);

    List<DataTypeGroupVO> listDataTypeGroups();

    Page<DataTypeItem> selectPage(DataTypeListParam param);

    boolean addDataTypeMapping(Long typeId, Long anotherTypeId);

    boolean addDataTypeMapping(List<DataTypeMappingParam> params);

    void addDataTypeMapping(MultiValueMap<Long, Long> dataTypeIdMapping);

    List<DataTypeMappingListVO> listDataTypeMappings(Long typeId);

    List<DataTypeMappingVO> listAllMappableDataTypes(Long typeId);

    List<SelectOptionVO> getSelectableTypes(String typeGroup);

    List<SelectOptionVO>  getSelectableTypeGroups();
}
