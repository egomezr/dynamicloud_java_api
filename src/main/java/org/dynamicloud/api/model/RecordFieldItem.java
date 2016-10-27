package org.dynamicloud.api.model;

/**
 * Represents the field's items
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 9/3/15
 **/
public class RecordFieldItem {
    private String value;
    private String text;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}