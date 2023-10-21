package io.devpl.generator.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.devpl.generator.common.mvc.EntityMapper;
import io.devpl.generator.domain.vo.DataTypeMappingListVO;
import io.devpl.generator.entity.DataTypeMapping;
import io.devpl.sdk.validation.Assert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 数据类型对应关系关联表
 **/
@Mapper
public interface DataTypeMappingMapper extends EntityMapper<DataTypeMapping> {

    List<DataTypeMappingListVO> listDataTypeMappingItems(@Param("typeId") Long typeId);

    default List<DataTypeMapping> selectListByIds(Collection<Long> ids) {
        Assert.notEmpty(ids, "ID列表为空");
        LambdaQueryWrapper<DataTypeMapping> qw = new LambdaQueryWrapper<>();
        qw.in(DataTypeMapping::getId, ids);
        return selectList(qw);
    }
}
