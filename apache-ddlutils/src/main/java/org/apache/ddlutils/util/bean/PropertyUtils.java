package org.apache.ddlutils.util.bean;

import org.apache.ddlutils.dynabean.DynaBean;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>Utility methods for using Java Reflection APIs to facilitate generic
 * property getter and setter operations on Java objects.</p>
 */
public class PropertyUtils {

    public static void setProperty(final DynaBean bean, final String name, final Object value)
        throws IllegalAccessException, InvocationTargetException,
        NoSuchMethodException {
        bean.set(name, value);
    }
}
