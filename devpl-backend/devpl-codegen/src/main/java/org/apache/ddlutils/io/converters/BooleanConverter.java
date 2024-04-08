package org.apache.ddlutils.io.converters;

public class BooleanConverter implements SqlTypeConverter {

    /**
     * The default value specified to our Constructor, if any.
     */
    private final Boolean defaultValue;

    /**
     * Should we return the default value on conversion errors?
     */
    private final boolean useDefault;

    public BooleanConverter() {
        this.defaultValue = null;
        this.useDefault = false;
    }

    public BooleanConverter(Boolean defaultValue) {
        this.defaultValue = defaultValue;
        this.useDefault = true;
    }

    @Override
    public Object fromString(String textRep, int sqlTypeCode) throws ConversionException {
        try {
            if (textRep.equalsIgnoreCase("yes") ||
                textRep.equalsIgnoreCase("y") ||
                textRep.equalsIgnoreCase("true") ||
                textRep.equalsIgnoreCase("on") ||
                textRep.equalsIgnoreCase("1")) {
                return (Boolean.TRUE);
            } else if (textRep.equalsIgnoreCase("no") ||
                       textRep.equalsIgnoreCase("n") ||
                       textRep.equalsIgnoreCase("false") ||
                       textRep.equalsIgnoreCase("off") ||
                       textRep.equalsIgnoreCase("0")) {
                return (Boolean.FALSE);
            } else if (useDefault) {
                return (defaultValue);
            } else {
                throw new ConversionException(textRep);
            }
        } catch (ClassCastException e) {
            if (useDefault) {
                return (defaultValue);
            }
            throw new ConversionException(e);
        }
    }
}
