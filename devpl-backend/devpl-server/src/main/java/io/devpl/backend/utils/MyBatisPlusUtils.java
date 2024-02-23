package io.devpl.backend.utils;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;

import java.util.Map;

public class MyBatisPlusUtils {

    /**
     * 填充实体值, 仅支持基本类型
     *
     * @param defaultValueMap key为数据库列名, value为值
     */
    public static void fillEntity(Object entity, Map<String, String> defaultValueMap, boolean useDbColumnKey) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
        StringBuilder sb = new StringBuilder();
        for (TableFieldInfo tableFieldInfo : tableInfo.getFieldList()) {
            try {
                tableFieldInfo.getField().set(entity, defaultValueMap.get(useDbColumnKey ? tableFieldInfo.getColumn() : tableFieldInfo.getProperty()));
            } catch (IllegalAccessException e) {
                sb.append(tableFieldInfo.getColumn());
            }
        }
    }
}
