package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import io.devpl.backend.domain.param.DataTypeListParam;
import io.devpl.backend.domain.param.DataTypeMappingListParam;
import io.devpl.backend.domain.vo.DataTypeMappingListVO;
import io.devpl.backend.domain.vo.DataTypeMappingVO;
import io.devpl.backend.domain.vo.MappedDataTypeVO;
import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.entity.DataTypeItem;
import io.devpl.backend.entity.DataTypeMapping;
import io.devpl.backend.entity.DataTypeMappingGroup;
import io.devpl.sdk.annotations.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * 类型映射 Service
 */
public interface DataTypeMappingService extends IService<DataTypeMapping> {

    /**
     * 查询所有类型映射规则分组
     *
     * @return 类型映射规则分组
     */
    List<SelectOptionVO> listMappingGroupOptions();

    /**
     * 按组 ID 查询列表
     *
     * @param groupId 组 ID
     * @return {@link List}<{@link DataTypeMapping}>
     */
    List<DataTypeMapping> listByGroupId(Long groupId);

    List<DataTypeMapping> listByTypeGroupKey(Number groupId, String typeGroupKey, String anotherTypeGroupKey);

    /**
     * 查询可添加类型映射的主类型
     *
     * @param param 类型列表查询参数
     * @return 类型信息列表
     */
    PageInfo<DataTypeItem> listSelectablePrimaryTypes(DataTypeListParam param);

    /**
     * 查询可被某个类型映射的其他类型
     *
     * @param param 类型列表查询参数
     * @return 类型信息列表
     */
    PageInfo<DataTypeItem> listSelectableAnotherTypes(DataTypeListParam param);

    /**
     * 新增类型映射分组
     *
     * @param group 分组
     * @return boolean
     */
    boolean addTypeMappingGroup(DataTypeMappingGroup group);

    List<DataTypeMappingVO> listAllUnMappedDataTypes();

    List<DataTypeMappingVO> listAllMappableDataTypes(Long typeId);

    List<DataTypeMappingListVO> selectMappingsByPrimaryType(DataTypeMappingListParam param);

    List<MappedDataTypeVO> listMappableDataTypes(Long groupId, Long typeId, String anotherTypeGroup);

    /**
     * 删除数据类型映射
     *
     * @param groupId        映射规则分组ID
     * @param typeId         主类型ID
     * @param anotherTypeIds 映射类型ID列表，可为空
     * @return 是否成功
     */
    boolean removeMappingByTypeId(Long groupId, Long typeId, @Nullable Collection<Long> anotherTypeIds);

    /**
     * 查询已经映射的数据类型ID
     *
     * @param groupId 映射规则分组ID
     * @param typeId  主类型ID
     * @return 已经映射的数据类型ID列表
     */
    List<Long> listMappedDataTypeId(Long groupId, Long typeId);
}
