package net.maku.maku.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.impl.BaseServiceImpl;
import net.maku.maku.convert.FieldSpecConvert;
import net.maku.maku.entity.FieldSpecEntity;
import net.maku.maku.query.FieldSpecQuery;
import net.maku.maku.vo.FieldSpecVO;
import net.maku.maku.dao.FieldSpecDao;
import net.maku.maku.service.FieldSpecService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* 字段信息表
*
* @author 111 222
* @since 1.0.0 2023-07-29
*/
@Service
@AllArgsConstructor
public class FieldSpecServiceImpl extends BaseServiceImpl
<FieldSpecDao, FieldSpecEntity> implements FieldSpecService {

@Override
public PageResult
<FieldSpecVO> page(FieldSpecQuery query) {
    IPage
    <FieldSpecEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(FieldSpecConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
        }

        private LambdaQueryWrapper
        <FieldSpecEntity> getWrapper(FieldSpecQuery query){
            LambdaQueryWrapper
            <FieldSpecEntity> wrapper = Wrappers.lambdaQuery();

                return wrapper;
                }

                @Override
                public void save(FieldSpecVO vo) {
                FieldSpecEntity entity = FieldSpecConvert.INSTANCE.convert(vo);

                baseMapper.insert(entity);
                }

                @Override
                public void update(FieldSpecVO vo) {
                FieldSpecEntity entity = FieldSpecConvert.INSTANCE.convert(vo);

                updateById(entity);
                }

                @Override
                @Transactional(rollbackFor = Exception.class)
                public void delete(List
                <Long> idList) {
                    removeByIds(idList);
                    }

                    }
