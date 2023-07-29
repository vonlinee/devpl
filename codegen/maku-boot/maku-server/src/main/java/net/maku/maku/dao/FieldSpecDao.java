package net.maku.maku.dao;

import net.maku.framework.mybatis.dao.BaseDao;
import net.maku.maku.entity.FieldSpecEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 字段信息表
*
* @author 111 222
* @since 1.0.0 2023-07-29
*/
@Mapper
public interface FieldSpecDao extends BaseDao
<FieldSpecEntity> {

    }
