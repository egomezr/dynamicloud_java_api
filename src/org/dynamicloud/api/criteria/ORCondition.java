package org.dynamicloud.api.criteria;

/**
 * This class represents an or condition.
 *
 * Implements condition to return a JSON according left and right parts.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/23/15
 **/
public class ORCondition implements Condition {
    private Condition left, right;

    /**
     * Will build an or condition using two part.
     *
     * @param left  left part of this or condition
     * @param right right part of this or condition
     */
    public ORCondition(Condition left, Condition right) {
        this.left = left;
        this.right = right;
    }

    /**
     * This method will return a String of this condition
     * @param parent this is the parent of this condition
     * @return a json
     */
    @Override
    public String toRecordString(Condition parent) {
        return (parent instanceof ORCondition ? "" : "\"$or\": {") + left.toRecordString(this) + "," +
                right.toRecordString(this) + (parent instanceof ORCondition ? "" : "}");
    }
}