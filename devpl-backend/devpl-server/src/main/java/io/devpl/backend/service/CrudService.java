package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 增删改查 Service
 *
 * @see com.baomidou.mybatisplus.extension.service.IService
 */
public interface CrudService {

    /**
     * 查询所有
     *
     * @param entityType 实体类型
     * @param <T>        实体类型
     * @return 列表
     */
    <T> List<T> listAll(Class<T> entityType);

    /**
     * 保存单条记录
     *
     * @param entity 实体类型
     * @param <T>    单条实体记录
     * @return 是否成功
     */
    <T> boolean save(T entity);

    /**
     * 根据主键更新
     *
     * @param entity 实体类型
     * @param <T>    单条实体记录
     * @return 是否成功
     */
    <T> boolean updateById(T entity);

    /**
     * 根据主键删除
     *
     * @param entityType 实体类型
     * @param id         主键ID值
     * @param <T>        单条实体记录
     * @return 是否成功
     */
    <T> boolean removeById(Class<T> entityType, Serializable id);

    /**
     * 移除表中所有数据
     *
     * @param entityType 实体类型
     * @param <T>        实体类型
     * @return 是否成功
     */
    <T> boolean removeAll(Class<T> entityType);

    /**
     * 保存或更新单条记录
     *
     * @param entity 实体类
     * @param <T>    实体类
     * @return 是否成功
     */
    <T> boolean saveOrUpdate(T entity);

    /**
     * 批量保存记录
     *
     * @param entities 实体列表
     * @param <T>      实体类
     * @return 是否成功
     */
    <T> boolean saveBatch(Collection<T> entities);

    /**
     * 批量保存记录
     *
     * @param entityType 实体类型
     * @param entities   实体列表
     * @param <T>        实体类
     * @return 是否成功
     */
    <T> boolean saveBatch(Class<T> entityType, Collection<T> entities);

    /**
     * 批量保存或者更新记录
     *
     * @param entities 实体列表
     * @param <T>      实体类
     * @return 是否成功
     */
    <T> boolean saveOrUpdateBatch(Collection<T> entities);

    /**
     * 分页查询
     *
     * @param entityType 实体类型
     * @param pageIndex  第几页
     * @param pageSize   每页大小
     * @param <T>        实体类
     * @return 是否成功
     */
    <T> Page<T> selectPage(Class<T> entityType, int pageIndex, int pageSize);
}
