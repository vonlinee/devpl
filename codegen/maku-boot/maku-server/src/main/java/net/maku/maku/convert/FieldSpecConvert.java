package net.maku.maku.convert;

import net.maku.maku.entity.FieldSpecEntity;
import net.maku.maku.vo.FieldSpecVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 字段信息表
*
* @author 111 222
* @since 1.0.0 2023-07-29
*/
@Mapper
public interface FieldSpecConvert {
FieldSpecConvert INSTANCE = Mappers.getMapper(FieldSpecConvert.class);

FieldSpecEntity convert(FieldSpecVO vo);

FieldSpecVO convert(FieldSpecEntity entity);

List
<FieldSpecVO> convertList(List
    <FieldSpecEntity> list);

        }
