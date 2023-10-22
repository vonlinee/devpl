package io.devpl.generator.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    <T> T selectOne(Class<T> entityType, QueryWrapper<T> qw);

    <T> List<T> list(Class<T> entityType);

    <T> boolean save(T entity);

    <T> boolean updateById(T entity);

    <T> boolean removeById(Class<T> entityType, Serializable id);

    <T> boolean saveOrUpdate(T entity);

    <T> boolean saveBatch(Collection<T> entities);

    <T> boolean saveBatch(Class<T> entityType, Collection<T> entities);

    <T> Page<T> selectPage(Class<T> entityType, int pageIndex, int pageSize);
}
