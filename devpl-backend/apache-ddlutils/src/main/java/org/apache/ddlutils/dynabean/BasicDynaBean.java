package org.apache.ddlutils.dynabean;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Minimal implementation of the <code>DynaBean</code> interface.  Can be
 * used as a convenience base class for more sophisticated implementations.</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - Instances of this class that are
 * accessed from multiple threads simultaneously need to be synchronized.</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - Instances of this class can be
 * successfully serialized and deserialized <strong>ONLY</strong> if all
 * property values are <code>Serializable</code>.</p>
 */
public class BasicDynaBean implements DynaBean, Serializable {

    // ---------------------------------------------------------- Constructors

    /**
     * The <code>DynaClass</code> "base class" that this DynaBean
     * is associated with.
     */
    protected DynaClass dynaClass;

    // ---------------------------------------------------- Instance Variables
    /**
     * The set of property values for this DynaBean, keyed by property name.
     */
    protected HashMap<String, Object> values = new HashMap<>();

    /**
     * Map decorator for this DynaBean
     */
    private transient Map<String, Object> mapDecorator;

    /**
     * Construct a new <code>DynaBean</code> associated with the specified
     * <code>DynaClass</code> instance.
     * @param dynaClass The DynaClass we are associated with
     */
    public BasicDynaBean(final DynaClass dynaClass) {
        super();
        this.dynaClass = dynaClass;
    }

    /**
     * Return a Map representation of this DynaBean.
     * </p>
     * This, for example, could be used in JSTL in the following way to access
     * a DynaBean's <code>fooProperty</code>:
     * <ul><li><code>${myDynaBean.<b>map</b>.fooProperty}</code></li></ul>
     * @return a Map representation of this DynaBean
     * @since 1.8.0
     */
    public Map<String, Object> getMap() {
        // cache the Map
        if (mapDecorator == null) {
            mapDecorator = new DynaBeanPropertyMapDecorator(this);
        }
        return mapDecorator;
    }

    // ------------------------------------------------------ DynaBean Methods

    /**
     * Does the specified mapped property contain a value for the specified
     * key value?
     * @param name Name of the property to check
     * @param key  Name of the key to check
     * @return <code>true</code> if the mapped property contains a value for
     * the specified key, otherwise <code>false</code>
     * @throws IllegalArgumentException if there is no property
     *                                  of the specified name
     */
    @Override
    public boolean contains(final String name, final String key) {

        final Object value = values.get(name);
        if (value == null) {
            throw new NullPointerException("No mapped value for '" + name + "(" + key + ")'");
        } else if (value instanceof Map) {
            return (((Map<?, ?>) value).containsKey(key));
        } else {
            throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'");
        }

    }

    /**
     * Return the value of a simple property with the specified name.
     * @param name Name of the property whose value is to be retrieved
     * @return The property's value
     * @throws IllegalArgumentException if there is no property
     *                                  of the specified name
     */
    @Override
    public Object get(final String name) {

        // Return any non-null value for the specified property
        final Object value = values.get(name);
        if (value != null) {
            return (value);
        }

        // Return a null value for a non-primitive property
        final Class<?> type = getDynaProperty(name).getType();
        if (!type.isPrimitive()) {
            return (null);
        }

        // Manufacture default values for primitive properties
        if (type == Boolean.TYPE) {
            return (Boolean.FALSE);
        } else if (type == Byte.TYPE) {
            return (byte) 0;
        } else if (type == Character.TYPE) {
            return (char) 0;
        } else if (type == Double.TYPE) {
            return 0.0D;
        } else if (type == Float.TYPE) {
            return 0.0F;
        } else if (type == Integer.TYPE) {
            return 0;
        } else if (type == Long.TYPE) {
            return 0L;
        } else if (type == Short.TYPE) {
            return (short) 0;
        } else {
            return (null);
        }
    }

    /**
     * Return the value of an indexed property with the specified name.
     * @param name  Name of the property whose value is to be retrieved
     * @param index Index of the value to be retrieved
     * @return The indexed property's value
     * @throws IllegalArgumentException  if there is no property
     *                                   of the specified name
     * @throws IllegalArgumentException  if the specified property
     *                                   exists, but is not indexed
     * @throws IndexOutOfBoundsException if the specified index
     *                                   is outside the range of the underlying property
     * @throws NullPointerException      if no array or List has been
     *                                   initialized for this property
     */
    @Override
    public Object get(final String name, final int index) {

        final Object value = values.get(name);
        if (value == null) {
            throw new NullPointerException("No indexed value for '" + name + "[" + index + "]'");
        } else if (value.getClass().isArray()) {
            return (Array.get(value, index));
        } else if (value instanceof List) {
            return ((List<?>) value).get(index);
        } else {
            throw new IllegalArgumentException("Non-indexed property for '" + name + "[" + index + "]'");
        }

    }

    /**
     * Return the value of a mapped property with the specified name,
     * or <code>null</code> if there is no value for the specified key.
     * @param name Name of the property whose value is to be retrieved
     * @param key  Key of the value to be retrieved
     * @return The mapped property's value
     * @throws IllegalArgumentException if there is no property
     *                                  of the specified name
     * @throws IllegalArgumentException if the specified property
     *                                  exists, but is not mapped
     */
    @Override
    public Object get(final String name, final String key) {

        final Object value = values.get(name);
        if (value == null) {
            throw new NullPointerException("No mapped value for '" + name + "(" + key + ")'");
        } else if (value instanceof Map) {
            return (((Map<?, ?>) value).get(key));
        } else {
            throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'");
        }

    }

    /**
     * Return the <code>DynaClass</code> instance that describes the set of
     * properties available for this DynaBean.
     * @return The associated DynaClass
     */
    @Override
    public DynaClass getDynaClass() {

        return (this.dynaClass);

    }

    /**
     * Remove any existing value for the specified key on the
     * specified mapped property.
     * @param name Name of the property for which a value is to
     *             be removed
     * @param key  Key of the value to be removed
     * @throws IllegalArgumentException if there is no property
     *                                  of the specified name
     */
    @Override
    public void remove(final String name, final String key) {

        final Object value = values.get(name);
        if (value == null) {
            throw new NullPointerException("No mapped value for '" + name + "(" + key + ")'");
        } else if (value instanceof Map) {
            ((Map<?, ?>) value).remove(key);
        } else {
            throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'");
        }

    }

    /**
     * Set the value of a simple property with the specified name.
     * @param name  Name of the property whose value is to be set
     * @param value Value to which this property is to be set
     * @throws ClassCastException       if the specified value cannot be
     *                                  converted to the type required for this property
     * @throws IllegalArgumentException if there is no property
     *                                  of the specified name
     * @throws NullPointerException     if an attempt is made to set a
     *                                  primitive property to null
     */
    @Override
    public void set(final String name, final Object value) {

        final DynaProperty descriptor = getDynaProperty(name);
        if (value == null) {
            if (descriptor.getType().isPrimitive()) {
                throw new NullPointerException("Primitive value for '" + name + "'");
            }
        } else if (!isAssignable(descriptor.getType(), value.getClass())) {
            throw new ClassCastException("Cannot assign value of type '" + value.getClass()
                .getName() + "' to property '" + name + "' of type '" + descriptor.getType().getName() + "'");
        }
        values.put(name, value);

    }

    /**
     * Set the value of an indexed property with the specified name.
     * @param name  Name of the property whose value is to be set
     * @param index Index of the property to be set
     * @param value Value to which this property is to be set
     * @throws ClassCastException        if the specified value cannot be
     *                                   converted to the type required for this property
     * @throws IllegalArgumentException  if there is no property
     *                                   of the specified name
     * @throws IllegalArgumentException  if the specified property
     *                                   exists, but is not indexed
     * @throws IndexOutOfBoundsException if the specified index
     *                                   is outside the range of the underlying property
     */
    @Override
    public void set(final String name, final int index, final Object value) {
        final Object prop = values.get(name);
        if (prop == null) {
            throw new NullPointerException("No indexed value for '" + name + "[" + index + "]'");
        } else if (prop.getClass().isArray()) {
            Array.set(prop, index, value);
        } else if (prop instanceof List) {
            // This is safe to cast because list properties are always of type Object
            @SuppressWarnings("unchecked")
            final List<Object> list = (List<Object>) prop;
            list.set(index, value);
        } else {
            throw new IllegalArgumentException("Non-indexed property for '" + name + "[" + index + "]'");
        }
    }

    /**
     * Set the value of a mapped property with the specified name.
     * @param name  Name of the property whose value is to be set
     * @param key   Key of the property to be set
     * @param value Value to which this property is to be set
     * @throws ClassCastException       if the specified value cannot be
     *                                  converted to the type required for this property
     * @throws IllegalArgumentException if there is no property
     *                                  of the specified name
     * @throws IllegalArgumentException if the specified property
     *                                  exists, but is not mapped
     */
    @Override
    public void set(final String name, final String key, final Object value) {
        final Object prop = values.get(name);
        if (prop == null) {
            throw new NullPointerException("No mapped value for '" + name + "(" + key + ")'");
        } else if (prop instanceof Map) {
            @SuppressWarnings("unchecked")
            // This is safe to cast because mapped properties are always
            // maps of types String -> Object
            final Map<String, Object> map = (Map<String, Object>) prop;
            map.put(key, value);
        } else {
            throw new IllegalArgumentException("Non-mapped property for '" + name + "(" + key + ")'");
        }
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Return the property descriptor for the specified property name.
     * @param name Name of the property for which to retrieve the descriptor
     * @return The property descriptor
     * @throws IllegalArgumentException if this is not a valid property
     *                                  name for our DynaClass
     */
    protected DynaProperty getDynaProperty(final String name) {

        final DynaProperty descriptor = getDynaClass().getDynaProperty(name);
        if (descriptor == null) {
            throw new IllegalArgumentException("Invalid property name '" + name + "'");
        }
        return (descriptor);

    }

    /**
     * Is an object of the source class assignable to the destination class?
     * @param dest   Destination class
     * @param source Source class
     * @return <code>true</code> if the source class is assignable to the
     * destination class, otherwise <code>false</code>
     */
    protected boolean isAssignable(final Class<?> dest, final Class<?> source) {
        return dest.isAssignableFrom(source) || ((dest == Boolean.TYPE) && (source == Boolean.class)) || ((dest == Byte.TYPE) && (source == Byte.class)) || ((dest == Character.TYPE) && (source == Character.class)) || ((dest == Double.TYPE) && (source == Double.class)) || ((dest == Float.TYPE) && (source == Float.class)) || ((dest == Integer.TYPE) && (source == Integer.class)) || ((dest == Long.TYPE) && (source == Long.class)) || ((dest == Short.TYPE) && (source == Short.class));
    }
}
