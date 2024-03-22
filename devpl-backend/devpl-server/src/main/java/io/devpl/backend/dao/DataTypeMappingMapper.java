package io.devpl.backend.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.domain.vo.DataTypeMappingListVO;
import io.devpl.backend.domain.vo.DataTypeMappingVO;
import io.devpl.backend.entity.DataTypeItem;
import io.devpl.backend.entity.DataTypeMapping;
import io.devpl.sdk.validation.Assert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 数据类型对应关系关联表
 **/
@Mapper
public interface DataTypeMappingMapper extends MyBatisPlusMapper<DataTypeMapping> {

    List<DataTypeMappingListVO> listDataTypeMappings(@Param("typeId") Long typeId);

    default List<DataTypeMapping> selectListByIds(Collection<? extends Serializable> ids) {
        Assert.notEmpty(ids, "ID列表为空");
        LambdaQueryWrapper<DataTypeMapping> qw = new LambdaQueryWrapper<>();
        qw.in(DataTypeMapping::getId, ids);
        return selectList(qw);
    }

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
    List<DataTypeItem> selectUnMappedTypeList();

    /**
     * 查询类型组不为指定值和类型ID不在指定集合中的数据类型
     *
     * @param excludeTypeGroupId 排除的类型分组
     * @param excludeIds         需要排除的类型ID列表
     * @return 类型列表
     */
    List<DataTypeItem> selectExcludeByTypeId(@Param("excludeTypeGroupId") String excludeTypeGroupId, @Param("excludeIds") Collection<Long> excludeIds);

    /**
     * 查询某个类型已经映射过的类型ID列表
     *
     * @param groupId 类型映射分组ID
     * @param typeId  类型ID
     * @return 已经映射过的类型ID列表
     */
    Set<Long> selectMappedDataTypeIds(Long groupId, Long typeId);
}
