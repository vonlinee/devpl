package net.maku.maku.service;

import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.BaseService;
import net.maku.maku.vo.FieldSpecVO;
import net.maku.maku.query.FieldSpecQuery;
import net.maku.maku.entity.FieldSpecEntity;

import java.util.List;

/**
* 字段信息表
*
* @author 111 222
* @since 1.0.0 2023-07-29
*/
public interface FieldSpecService extends BaseService
<FieldSpecEntity> {

    PageResult
    <FieldSpecVO> page(FieldSpecQuery query);

        void save(FieldSpecVO vo);

        void update(FieldSpecVO vo);

        void delete(List
        <Long> idList);
            }
