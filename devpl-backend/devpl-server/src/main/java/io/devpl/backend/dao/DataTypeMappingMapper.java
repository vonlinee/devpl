package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.domain.param.DataTypeListParam;
import io.devpl.backend.domain.param.DataTypeMappingListParam;
import io.devpl.backend.domain.vo.DataTypeMappingListVO;
import io.devpl.backend.domain.vo.DataTypeMappingVO;
import io.devpl.backend.entity.DataTypeItem;
import io.devpl.backend.entity.DataTypeMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * 数据类型对应关系关联表
 **/
@Mapper
public interface DataTypeMappingMapper extends MyBatisPlusMapper<DataTypeMapping> {

    /**
     * 根据主类型查询该类型的映射类型
     *
     * @param param 查询参数
     * @return 映射类型列表
     */
    List<DataTypeMappingListVO> selectMappingsByPrimaryType(@Param("param") DataTypeMappingListParam param);

    /**
     * 根据类型分组查询，查询某个类型分组到另外一个类型分组所有的映射关系
     * 一对一映射
     *
     * @param groupId            类型映射分组
     * @param typeGroupId        类型分组
     * @param anotherTypeGroupId 另一个类型分组
     * @return 类型映射列表
     */
    List<DataTypeMapping> selectListByTypeGroupId(
        @Param("groupId") Number groupId,
        @Param("typeGroupId") String typeGroupId,
        @Param("anotherTypeGroupId") String anotherTypeGroupId);

    /**
     * 查询所有没有设置过类型映射的数据类型
     *
     * @return 数据类型列表
     */
    @Select(value = """
        SELECT A.id AS type_id, dtg.group_id AS type_group_id, A.type_key, A.locale_type_name, false as mapped
        FROM data_type_item A
                 LEFT JOIN (SELECT DISTINCT type_id FROM data_type_mapping) B ON A.id = B.type_id
                 LEFT JOIN data_type_group dtg on A.type_group_id = dtg.group_id
        WHERE B.type_id IS NULL
        """)
    List<DataTypeMappingVO> listAllUnMappedDataTypes();

    /**
     * 查询某个类型映射的数据类型列表
     *
     * @param typeId 主类型
     * @return 映射的数据类型列表
     */
    @Select(value = "SELECT * FROM data_type_item WHERE id != #{typeId}")
    List<DataTypeMappingVO> listAllMappableDataTypes(Long typeId);

    /**
     * 查询未映射过的所有数据类型
     *
     * @return 未映射过的所有数据类型
     */
    List<DataTypeItem> selectUnMappedTypeList(@Param("param") DataTypeListParam param);

    /**
     * 查询类型组不为指定值和类型ID不在指定集合中的数据类型
     *
     * @param param DataTypeListParam
     * @return 类型列表
     */
    List<DataTypeItem> selectExcludeByTypeId(@Param("param") DataTypeListParam param);

    /**
     * 查询某个类型已经映射过的类型ID列表
     *
     * @param groupId 类型映射分组ID，可为空
     * @param typeId  类型ID
     * @return 已经映射过的类型ID列表
     */
    Set<Long> selectMappedDataTypeIds(@Param("groupId") Long groupId, @Param("typeId") Long typeId);
}
