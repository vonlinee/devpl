package org.apache.ddlutils.alteration;


import org.apache.ddlutils.model.Database;

/**
 * Marker interface for changes to a database model element.
 */
public interface ModelChange {
    /**
     * Applies this change to the given database.
     *
     * @param database      The database
     * @param caseSensitive Whether the case of names matters
     */
    void apply(Database database, boolean caseSensitive);
}
