package org.apache.ddlutils.util;

/**
 * Abstract pair class to assist with creating KeyValue and MapEntry implementations.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 1.3 $ $Date: 2004/02/18 01:00:08 $
 *
 * @author James Strachan
 * @author Michael A. Smith
 * @author Neil O'Toole
 * @author Stephen Colebourne
 */
public abstract class AbstractKeyValue implements KeyValue {

    /** The key */
    protected Object key;
    /** The value */
    protected Object value;

    /**
     * Constructs a new pair with the specified key and given value.
     *
     * @param key  the key for the entry, may be null
     * @param value  the value for the entry, may be null
     */
    protected AbstractKeyValue(Object key, Object value) {
        super();
        this.key = key;
        this.value = value;
    }

    /**
     * Gets the key from the pair.
     *
     * @return the key
     */
    public Object getKey() {
        return key;
    }

    /**
     * Gets the value from the pair.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Gets a debugging String view of the pair.
     *
     * @return a String view of the entry
     */
    public String toString() {
        return String.valueOf(getKey()) +
            '=' +
            getValue();
    }
}
