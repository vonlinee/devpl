package org.apache.ddlutils.io;

import org.apache.ddlutils.dynabean.DynaBean;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents an object waiting for insertion into the database. Is used by the
 * {@link org.apache.ddlutils.io.DataToDatabaseSink} to insert the objects in the correct
 * order according to their foreign keys.
 */
public class WaitingObject {
    /**
     * The object that is waiting for insertion.
     */
    private final DynaBean _obj;
    /**
     * The original identity of the waiting object.
     */
    private final Identity _objIdentity;
    /**
     * The identities of the waited-for objects.
     */
    private final List<Identity> _waitedForIdentites = new LinkedList<>();

    /**
     * Creates a new <code>WaitingObject</code> instance for the given object.
     * @param obj         The object that is waiting
     * @param objIdentity The (original) identity of the object
     */
    public WaitingObject(DynaBean obj, Identity objIdentity) {
        _obj = obj;
        _objIdentity = objIdentity;
    }

    /**
     * Returns the waiting object.
     * @return The object
     */
    public DynaBean getObject() {
        return _obj;
    }

    /**
     * Adds the identity of another object that the object is waiting for.
     * @param fkIdentity The identity of the waited-for object
     */
    public void addPendingFK(Identity fkIdentity) {
        _waitedForIdentites.add(fkIdentity);
    }

    /**
     * Returns the identities of the object that this object is waiting for.
     * @return The identities
     */
    public Iterator<Identity> getPendingFKs() {
        return _waitedForIdentites.iterator();
    }

    /**
     * Removes the specified identity from list of identities of the waited-for objects.
     * @param fkIdentity The identity to remove
     * @return The removed identity if any
     */
    public Identity removePendingFK(Identity fkIdentity) {
        Identity result = null;
        int idx = _waitedForIdentites.indexOf(fkIdentity);

        if (idx >= 0) {
            result = _waitedForIdentites.get(idx);
            _waitedForIdentites.remove(idx);
        }
        return result;
    }

    /**
     * Determines whether there are any identities of waited-for objects
     * registered with this waiting object.
     * @return <code>true</code> if identities of waited-for objects are registered
     */
    public boolean hasPendingFKs() {
        return !_waitedForIdentites.isEmpty();
    }

    @Override
    public String toString() {
        return _objIdentity +
               " waiting for " +
               _waitedForIdentites;
    }
}
