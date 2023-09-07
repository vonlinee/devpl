package org.apache.ddlutils.util.bean;

import java.lang.reflect.InvocationTargetException;

public class BeanUtils {

    /**
     * <p>Set the specified property value, performing type conversions as
     * required to conform to the type of the destination property.</p>
     *
     * <p>For more details see <code>BeanUtilsBean</code>.</p>
     * @param bean  Bean on which setting is to be performed
     * @param name  Property name (can be nested/indexed/mapped/combo)
     * @param value Value to be set
     * @throws IllegalAccessException    if the caller does not have
     *                                   access to the property accessor method
     * @throws InvocationTargetException if the property accessor method
     *                                   throws an exception
     */
    public static void setProperty(final Object bean, final String name, final Object value)
        throws IllegalAccessException, InvocationTargetException {

    }

    /**
     * <p>Copy property values from the origin bean to the destination bean
     * for all cases where the property names are the same.</p>
     *
     * <p>For more details see <code>BeanUtilsBean</code>.</p>
     * @param dest Destination bean whose properties are modified
     * @param orig Origin bean whose properties are retrieved
     * @throws IllegalAccessException    if the caller does not have
     *                                   access to the property accessor method
     * @throws IllegalArgumentException  if the <code>dest</code> or
     *                                   <code>orig</code> argument is null or if the <code>dest</code>
     *                                   property type is different from the source type and the relevant
     *                                   converter has not been registered.
     * @throws InvocationTargetException if the property accessor method
     *                                   throws an exception
     */
    public static void copyProperties(final Object dest, final Object orig)
        throws IllegalAccessException, InvocationTargetException {

    }
}
