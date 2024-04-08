package org.apache.ddlutils.alteration;


/**
 * Represents a pair of objects.
 */
public class Pair<V> {
    /**
     * The first object.
     */
    private final V _firstObj;
    /**
     * The first object.
     */
    private final V _secondObj;

    /**
     * Creates a pair object.
     *
     * @param firstObj  The first object
     * @param secondObj The second object
     */
    public Pair(V firstObj, V secondObj) {
        _firstObj = firstObj;
        _secondObj = secondObj;
    }

    /**
     * Returns the first object of the pair.
     *
     * @return The first object
     */
    public V getFirst() {
        return _firstObj;
    }

    /**
     * Returns the second object of the pair.
     *
     * @return The second object
     */
    public V getSecond() {
        return _secondObj;
    }
}
