package org.apache.ddlutils.dynabean;

/**
 * <p>A specialized extension to <code>DynaClass</code> that allows properties
 * to be added or removed dynamically.</p>
 *
 * <p><strong>WARNING</strong> - No guarantees that this will be in the final
 * APIs ... it's here primarily to preserve some concepts that were in the
 * original proposal for further discussion.</p>
 */
public interface MutableDynaClass extends DynaClass {

    /**
     * Add a new dynamic property with no restrictions on data type,
     * readability, or writeability.
     * @param name Name of the new dynamic property
     * @throws IllegalArgumentException if name is null
     * @throws IllegalStateException    if this DynaClass is currently
     *                                  restricted, so no new properties can be added
     */
    void add(String name);

    /**
     * Add a new dynamic property with the specified data type, but with
     * no restrictions on readability or writeability.
     * @param name Name of the new dynamic property
     * @param type Data type of the new dynamic property (null for no
     *             restrictions)
     * @throws IllegalArgumentException if name is null
     * @throws IllegalStateException    if this DynaClass is currently
     *                                  restricted, so no new properties can be added
     */
    void add(String name, Class<?> type);

    /**
     * Add a new dynamic property with the specified data type, readability,
     * and writeability.
     * @param name      Name of the new dynamic property
     * @param type      Data type of the new dynamic property (null for no
     *                  restrictions)
     * @param readable  Set to <code>true</code> if this property value
     *                  should be readable
     * @param writeable Set to <code>true</code> if this property value
     *                  should be writeable
     * @throws IllegalArgumentException if name is null
     * @throws IllegalStateException    if this DynaClass is currently
     *                                  restricted, so no new properties can be added
     */
    void add(String name, Class<?> type, boolean readable, boolean writeable);

    /**
     * Is this DynaClass currently restricted, if so, no changes to the
     * existing registration of property names, data types, readability, or
     * writeability are allowed.
     * @return <code>true</code> if this Mutable {@link DynaClass} is restricted,
     * otherwise <code>false</code>
     */
    boolean isRestricted();

    /**
     * Set the restricted state of this DynaClass to the specified value.
     * @param restricted The new restricted state
     */
    void setRestricted(boolean restricted);

    /**
     * Remove the specified dynamic property, and any associated data type,
     * readability, and writeability, from this dynamic class.
     * <strong>NOTE</strong> - This does <strong>NOT</strong> cause any
     * corresponding property values to be removed from DynaBean instances
     * associated with this DynaClass.
     * @param name Name of the dynamic property to remove
     * @throws IllegalArgumentException if name is null
     * @throws IllegalStateException    if this DynaClass is currently
     *                                  restricted, so no properties can be removed
     */
    void remove(String name);
}
