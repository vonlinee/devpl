package org.apache.ddlutils.model;

import java.util.Objects;

/**
 * Represents the different cascade actions for the <code>onDelete</code> and
 * <code>onUpdate</code> properties of {@link ForeignKey}.
 */
public enum CascadeActionEnum {

    /**
     * The enum value for a cascade action which directs the database to apply the change to
     * the referenced table also to this table. E.g. if the referenced row is deleted, then
     * the local one will also be deleted when this value is used for the onDelete action.
     */
    CASCADE("cascade", 1),

    /**
     * The enum value for a cascade action which directs the database to set the local columns
     * referenced by the foreign key to null when the referenced row changes/is deleted.
     */
    SET_NULL("setnull", 2),

    /**
     * The enum value for a cascade action which directs the database to set the local columns
     * referenced by the foreign key to the default value when the referenced row changes/is deleted.
     */
    SET_DEFAULT("setdefault", 3),

    /**
     * The enum value for a cascade action which directs the database to restrict the change
     * changes to the referenced column. The interpretation of this is database-dependent, but it is
     * usually the same as {@link #NONE}.
     */
    RESTRICT("restrict", 4),

    /**
     * The enum value for the cascade action that directs the database to not change the local column
     * when the value of the referenced column changes, only check the foreign key constraint.
     */
    NONE("none", 5);

    private final String defaultTextRep;
    private final int value;

    /**
     * Creates a new enum object.
     *
     * @param defaultTextRep The textual representation
     * @param value          The corresponding integer value
     */
    CascadeActionEnum(String defaultTextRep, int value) {
        this.defaultTextRep = defaultTextRep;
        this.value = value;
    }

    /**
     * Returns the enum value that corresponds to the given textual
     * representation.
     *
     * @param defaultTextRep The textual representation
     * @return The enum value
     */
    public static CascadeActionEnum getEnum(String defaultTextRep) {
        for (CascadeActionEnum item : CascadeActionEnum.values()) {
            if (Objects.equals(item.defaultTextRep, defaultTextRep)) {
                return item;
            }
        }
        return null;
    }

    public String getName() {
        return defaultTextRep;
    }

    public int getValue() {
        return value;
    }
}
