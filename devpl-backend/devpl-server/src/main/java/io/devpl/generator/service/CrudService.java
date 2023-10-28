package io.devpl.generator.service;

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

    <T> List<T> listAll(Class<T> entityType);

    <T> boolean save(T entity);

    <T> boolean updateById(T entity);

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

    <T> boolean saveBatch(Collection<T> entities);

    <T> boolean saveBatch(Class<T> entityType, Collection<T> entities);

    <T> Page<T> selectPage(Class<T> entityType, int pageIndex, int pageSize);
}
