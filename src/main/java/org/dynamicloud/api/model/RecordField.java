package org.dynamicloud.api.model;

/**
 * Represents a field in Dynamicloud.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 9/2/15
 **/
public class RecordField {
    private Long id;
    private String identifier;
    private String label;
    private String comment;
    private boolean uniqueness;
    private boolean required;
    private RecordFieldType type;
    private RecordFieldItem [] items;

    private Long mid;

    /**
     * Constructs a field using the related model id
     * @param mid model id
     */
    public RecordField(Long mid) {
        this.mid = mid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isUniqueness() {
        return uniqueness;
    }

    public void setUniqueness(boolean uniqueness) {
        this.uniqueness = uniqueness;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public RecordFieldType getType() {
        return type;
    }

    public void setType(RecordFieldType type) {
        this.type = type;
    }

    public RecordFieldItem[] getItems() {
        return items;
    }

    public void setItems(RecordFieldItem[] items) {
        this.items = items;
    }

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }
}