package io.devpl.generator.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collection;

/**
 * 增删改查 Service
 *
 * @see com.baomidou.mybatisplus.extension.service.IService
 */
public interface CrudService {

    <T> boolean saveOrUpdate(T entity);

    <T> boolean saveBatch(Collection<T> entities);

    <T> boolean saveBatch(Class<T> entityType, Collection<T> entities);

    <T> Page<T> selectPage(Class<T> entityType, int pageIndex, int pageSize);
}
