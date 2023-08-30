package org.apache.ddlutils.alteration;

/**
 * Represents a pair of objects.
 * @version $Revision: $
 */
public class Pair {
    /**
     * The first object.
     */
    private final Object _firstObj;
    /**
     * The first object.
     */
    private final Object _secondObj;

    /**
     * Creates a pair object.
     * @param firstObj  The first object
     * @param secondObj The second object
     */
    public Pair(Object firstObj, Object secondObj) {
        _firstObj = firstObj;
        _secondObj = secondObj;
    }

    /**
     * Returns the first object of the pair.
     * @return The first object
     */
    public Object getFirst() {
        return _firstObj;
    }

    /**
     * Returns the second object of the pair.
     * @return The second object
     */
    public Object getSecond() {
        return _secondObj;
    }
}
