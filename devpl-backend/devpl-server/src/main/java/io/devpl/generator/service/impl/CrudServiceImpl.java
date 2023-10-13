package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import io.devpl.generator.service.CrudService;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

/**
 * 增删改查 Service
 */
@Service
public class CrudServiceImpl implements CrudService {

    private static final Log log = LogFactory.getLog(CrudService.class);

    /**
     * 批处理条数
     */
    private final int batchSize = 1000;

    /**
     * 数据为空时返回的结果
     */
    private final boolean resultWhenEmpty = true;

    @Override
    public <T> boolean saveOrUpdate(T entity) {
        Class<?> entityType = entity.getClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityType);
        String keyProperty = tableInfo.getKeyProperty();
        Object fieldValue = ReflectionKit.getFieldValue(entity, keyProperty);
        try (SqlSession sqlSession = SqlHelper.sqlSession(entityType)) {
            int res;
            if (fieldValue == null) {
                String insertOneStatement = tableInfo.getCurrentNamespace() + "." + SqlMethod.INSERT_ONE.getMethod();
                res = sqlSession.insert(insertOneStatement, entity);
            } else {
                String updateByIdStatement = tableInfo.getCurrentNamespace() + "." + SqlMethod.UPDATE_BY_ID.getMethod();
                res = sqlSession.update(updateByIdStatement, entity);
            }
            return res > 0;
        } catch (Exception exception) {
            log.error("saveOrUpdate", exception);
        }
        return false;

    }

    @Override
    public <T> boolean saveBatch(Collection<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return resultWhenEmpty;
        }
        Iterator<T> iterator = entities.iterator();
        T first = iterator.next();
        Class<?> entityType = first.getClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityType);
        String statement = tableInfo.getCurrentNamespace() + "." + SqlMethod.INSERT_ONE.getMethod();
        return SqlHelper.executeBatch(entityType, log, sqlSession -> {
            sqlSession.insert(statement, first);
            int i = 1;
            while (iterator.hasNext()) {
                sqlSession.insert(statement, iterator.next());
                i++;
                if (i >= batchSize) {
                    sqlSession.flushStatements();
                    i = 0;
                }
            }
        });
    }

    @Override
    public <T> boolean saveBatch(Class<T> entityType, Collection<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return resultWhenEmpty;
        }
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityType);
        String statement = tableInfo.getCurrentNamespace() + "." + SqlMethod.INSERT_ONE.getMethod();
        return SqlHelper.executeBatch(entityType, log, entities, batchSize, (sqlSession, entity) -> sqlSession.insert(statement, entity));
    }

    @Override
    public <T> Page<T> selectPage(Class<T> entityType, int pageIndex, int pageSize) {
        Page<T> page = new Page<>(pageIndex, pageSize);
        try (SqlSession sqlSession = SqlHelper.sqlSession(entityType)) {
            BaseMapper<T> mapper = SqlHelper.getMapper(entityType, sqlSession);
            return mapper.selectPage(page, Wrappers.emptyWrapper());
        } catch (Exception exception) {
            log.error("selectPage", exception);
        }
        return page;
    }
}
