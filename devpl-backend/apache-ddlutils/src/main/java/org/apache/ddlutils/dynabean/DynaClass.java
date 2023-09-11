package org.apache.ddlutils.dynabean;

/**
 * <p>A <strong>DynaClass</strong> is a simulation of the functionality of
 * <code>java.lang.Class</code> for classes implementing the
 * <code>DynaBean</code> interface.  DynaBean instances that share the same
 * DynaClass all have the same set of available properties, along with any
 * associated data types, read-only states, and write-only states.</p>
 * @version $Id$
 */

public interface DynaClass {

    /**
     * Return the name of this DynaClass (analogous to the
     * <code>getName()</code> method of <code>java.lang.Class</code), which
     * allows the same <code>DynaClass</code> implementation class to support
     * different dynamic classes, with different sets of properties.
     * @return the name of the DynaClass
     */
    String getName();

    /**
     * Return a property descriptor for the specified property, if it exists;
     * otherwise, return <code>null</code>.
     * @param name Name of the dynamic property for which a descriptor
     *             is requested
     * @return The descriptor for the specified property
     * @throws IllegalArgumentException if no property name is specified
     */
    DynaProperty getDynaProperty(String name);

    /**
     * <p>Return an array of <code>ProperyDescriptors</code> for the properties
     * currently defined in this DynaClass.  If no properties are defined, a
     * zero-length array will be returned.</p>
     *
     * <p><strong>FIXME</strong> - Should we really be implementing
     * <code>getBeanInfo()</code> instead, which returns property descriptors
     * and a bunch of other stuff?</p>
     * @return the DynaProperties of properties for this DynaClass
     */
    DynaProperty[] getDynaProperties();

    /**
     * Instantiate and return a new DynaBean instance, associated
     * with this DynaClass.
     * @return A new <code>DynaBean</code> instance
     * @throws IllegalAccessException if the Class or the appropriate
     *                                constructor is not accessible
     * @throws InstantiationException if this Class represents an abstract
     *                                class, an array class, a primitive type, or void; or if instantiation
     *                                fails for some other reason
     */
    DynaBean newInstance() throws IllegalAccessException, InstantiationException;
}