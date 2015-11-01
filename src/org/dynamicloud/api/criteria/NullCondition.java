package org.dynamicloud.api.criteria;

/**
 * This class represents an is null or is not null condition.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/24/15
 **/
public class NullCondition implements Condition {
    private String left;
    private boolean isNotNull;

    /**
     * Build the condition using left part and flag that indicates what kind of comparison must be used.
     *
     * @param left attribute to compare.
     * @param isNotNull flag that indicates what kind of comparison.
     */
    public NullCondition(String left, boolean isNotNull) {
        this.left = left;
        this.isNotNull = isNotNull;
    }

    /**
     * This method will return a String of this condition
     *
     * @param parent this is the parent of this condition
     * @return a string
     */
    @Override
    public String toRecordString(Condition parent) {
        return "\"" + left + "\": {" + (isNotNull ? "\"$notNull\"" : "\"$null\"") + ": \"1\"}";
    }
}
