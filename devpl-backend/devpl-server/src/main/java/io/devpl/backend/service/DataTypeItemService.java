package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.domain.param.DataTypeGroupParam;
import io.devpl.backend.domain.param.DataTypeListParam;
import io.devpl.backend.domain.param.DataTypeMappingAddParam;
import io.devpl.backend.domain.param.DataTypeMappingListParam;
import io.devpl.backend.domain.vo.DataTypeGroupVO;
import io.devpl.backend.domain.vo.DataTypeMappingListVO;
import io.devpl.backend.domain.vo.DataTypeMappingVO;
import io.devpl.backend.domain.vo.SelectOptionVO;
import io.devpl.backend.entity.DataTypeGroup;
import io.devpl.backend.entity.DataTypeItem;

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

    /**
     * 添加数据类型映射关系
     *
     * @param param 数据类型映射关系参数
     * @return 是否成功
     */
    boolean addDataTypeMapping(DataTypeMappingAddParam param);

    /**
     * 手动分页
     *
     * @param param 查询参数
     * @return 统计数量
     */
    long countDataType(DataTypeMappingListParam param);

    /**
     * 查询数据类型映射关系列表
     *
     * @param param 查询参数
     * @return 数据类型映射关系列表
     */
    List<DataTypeMappingListVO> listDataTypeMappings(DataTypeMappingListParam param);

    /**
     * 查询某个类型映射的所有其他类型
     *
     * @param typeId 主类型ID
     * @return 映射的数据类型
     */
    List<DataTypeMappingVO> listAllMappableDataTypes(Long typeId);

    /**
     * 获取某个分组可选择的类型列表，包含名称和ID
     *
     * @param typeGroupId 类型分组，为空则获取所有
     * @return 选项列表VO
     */
    List<SelectOptionVO> getSelectableTypes(String typeGroupId);

    /**
     * 查询所有类型分组列表
     *
     * @return 类型分组列表
     */
    List<SelectOptionVO> getSelectableTypeGroups(String excludeTypeGroupId);

    /**
     * 删除类型分组
     *
     * @param param 类型分组参数
     * @return 是否成功
     */
    boolean removeDataTypeGroupByIds(DataTypeGroupParam param);
}
