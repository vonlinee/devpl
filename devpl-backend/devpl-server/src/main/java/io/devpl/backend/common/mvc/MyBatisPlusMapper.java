package io.devpl.backend.common.mvc;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.devpl.backend.common.query.PageParam;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 扩展MyBatisPlus的Mapper
 * 在Mapper层封装批量操作，不用再使用Service
 */
public interface MyBatisPlusMapper<T> extends BaseMapper<T> {

    /**
     * 实体类类型
     *
     * @return 实体类类型
     */
    default Class<T> getEntityClass() {
        return null;
    }

    default boolean retBool(Integer result) {
        return SqlHelper.retBool(result);
    }

    /**
     * 查询单表所有数据
     *
     * @return 单表所有数据
     */
    default List<T> selectAll() {
        return selectList(Wrappers.emptyWrapper());
    }

    /**
     * 查分页的数据
     *
     * @param pageIndex 页码
     * @param pageSize  每页大小
     * @return 一页的数据
     */
    default List<T> selectList(int pageIndex, int pageSize) {
        return selectList(pageIndex, pageSize, Wrappers.emptyWrapper());
    }

    /**
     * 查分页的数据
     *
     * @param pageIndex    页码
     * @param pageSize     每页大小
     * @param queryWrapper 查询条件
     * @return 一页的数据
     */
    default List<T> selectList(int pageIndex, int pageSize, Wrapper<T> queryWrapper) {
        Page<T> page = selectPage(new Page<>(pageIndex, pageSize), queryWrapper);
        return page.getRecords();
    }

    /**
     * 分页查询
     *
     * @param param 分页参数
     * @param qw    查询Wrapper
     * @return 分页数据
     */
    default IPage<T> selectPage(PageParam param, Wrapper<T> qw) {
        return selectPage(param.asPage(), qw);
    }

    /**
     * 批量插入
     *
     * @param entities 实体列表
     * @return 是否成功
     */
    default boolean insertBatch(Collection<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return false;
        }
        if (entities.size() == 1) {
            for (T entity : entities) {
                return SqlHelper.retBool(insert(entity));
            }
        }
        return Db.saveBatch(entities);
    }

    /**
     * 批量插入
     * warning: Possible heap pollution from parameterized vararg type
     * <a href="https://stackoverflow.com/questions/12462079/possible-heap-pollution-via-varargs-parameter">...</a>
     *
     * @param entities 实体列表
     * @return 是否成功
     */
    default boolean insertBatch(T... entities) {
        if (entities == null || entities.length == 0) {
            return false;
        }
        if (entities.length == 1) {
            return SqlHelper.retBool(insert(entities[0]));
        }
        return Db.saveBatch(Arrays.asList(entities));
    }

    /**
     * 根据主键批量更新
     *
     * @param entities 实体列表
     * @return 是否成功
     */
    default boolean updateBatchById(Collection<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return false;
        }
        if (entities.size() == 1) {
            for (T entity : entities) {
                return SqlHelper.retBool(updateById(entity));
            }
        }
        return Db.updateBatchById(entities);
    }

    /**
     * 根据主键新增或者更新
     *
     * @param entities 实体列表
     * @return 是否成功
     */
    default boolean insertOrUpdateBatch(Collection<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return false;
        }
        if (entities.size() == 1) {
            for (T entity : entities) {
                return Db.saveOrUpdate(entity);
            }
        }
        return Db.saveOrUpdateBatch(entities);
    }

    /**
     * 新增或保存
     *
     * @param entity 实体
     * @return 是否成功
     */
    default boolean insertOrUpdate(T entity) {
        if (entity == null) {
            return false;
        }
        return Db.saveOrUpdate(entity);
    }

    /**
     * 清空所有数据
     *
     * @return 是否成功
     */
    default boolean deleteAll() {
        return Db.remove(Wrappers.emptyWrapper());
    }

    /**
     * 根据主键删除
     *
     * @param ids        主键
     * @param entityType 实体类型
     * @return 是否成功
     */
    default boolean deleteByIds(@Nullable Class<T> entityType, Collection<? extends Serializable> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        if (entityType == null) {
            entityType = getEntityClass();
            Assert.notNull(entityType, "entity type is null");
        } else {
            Class<T> _entityType = getEntityClass();
            if (entityType == _entityType) {
                List<? extends Serializable> idList = ids.stream().toList();
                if (idList.size() == 1) {
                    return SqlHelper.retBool(deleteById(idList.get(0)));
                }
            }
        }
        return Db.removeByIds(ids, entityType);
    }

    /**
     * 根据主键批量删除
     *
     * @param ids 主键列表
     * @return 是否成功
     */
    default boolean deleteByIds(Serializable... ids) {
        if (ids == null || ids.length == 0) {
            return false;
        }
        if (ids.length == 1) {
            return SqlHelper.retBool(deleteById(ids[0]));
        }
        Class<T> entityClass = getEntityClass();
        Assert.notNull(entityClass, "entity class is null");
        return Db.removeByIds(Arrays.asList(ids), entityClass);
    }

    /**
     * 根据主键删除
     *
     * @param ids        主键
     * @param entityType 实体类型
     * @return 是否成功
     */
    default boolean deleteByIds(@Nullable Class<T> entityType, Serializable... ids) {
        if (ids == null || ids.length == 0) {
            return false;
        }
        if (entityType == null) {
            if (ids.length == 1) {
                return SqlHelper.retBool(deleteById(ids[0]));
            }
            return false;
        }
        return Db.removeByIds(Arrays.asList(ids), entityType);
    }

    /**
     * 根据主键ID列表逻辑删除
     *
     * @param ids 主键ID列表
     * @return 是否成功
     */
    default boolean logicDeleteByIds(Serializable... ids) {
        if (ids == null || ids.length == 0) {
            return false;
        }
        UpdateWrapper<T> qw = new UpdateWrapper<>();
        qw.set("is_deleted", 1);
        qw.in("id", (Object[]) ids);
        return SqlHelper.retBool(update(qw));
    }
}
