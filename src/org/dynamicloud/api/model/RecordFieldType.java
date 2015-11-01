package org.dynamicloud.api.model;

/**
 * Indicates the different field's types in DynamiCloud
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 9/2/15
 **/
public enum RecordFieldType {
    TEXT(1), NUMBER(10), CHECKBOX(2), RADIO_BUTTON(3), SELECT(4), SELECT_MULTI_SELECTION(5), TEXTAREA(6), BIG_TEXT(7), PASSWORD(8), DATE(9);

    private final int type;

    /**
     * Build the enum
     *
     * @param type type value
     */
    RecordFieldType(int type) {
        this.type = type;
    }

    /**
     * Gets the type value
     *
     * @return the type value
     */
    public int getType() {
        return type;
    }

    /**
     * Gets the FieldType according type parameter
     *
     * @param type target
     * @return RecordTypeField
     */
    public static RecordFieldType getFieldType(int type) {
        switch (type) {
            case 1:
                return TEXT;
            case 10:
                return NUMBER;
            case 2:
                return CHECKBOX;
            case 3:
                return RADIO_BUTTON;
            case 4:
                return SELECT;
            case 5:
                return SELECT_MULTI_SELECTION;
            case 6:
                return TEXTAREA;
            case 7:
                return BIG_TEXT;
            case 8:
                return PASSWORD;
            case 9:
                return DATE;
        }

        throw new IllegalStateException("Illegal type '" + type + "'");
    }
}