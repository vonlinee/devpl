package org.apache.ddlutils.util;

import org.apache.ddlutils.dynabean.SqlDynaBean;

import javax.sql.DataSource;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;

/**
 * TODO 移植beanutils的逻辑
 */
public class BeanUtils {

    public static void copyProperties(SqlDynaBean answer, Object source) throws InvocationTargetException, IllegalAccessException {

    }

    public static void setProperty(DataSource dataSource, String substring, Object value) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(dataSource.getClass());
            BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();

            beanDescriptor.setValue(substring, value);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }
}
