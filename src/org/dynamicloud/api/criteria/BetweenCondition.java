package org.dynamicloud.api.criteria;

/**
 * This class represents a between condition.
 * <p/>
 * How to use: Conditions.between("age", 20, 30);
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 11/28/15
 **/
public class BetweenCondition implements Condition {
    private String field;
    private Object left, right;

    /**
     * Builds an instance with a specific field whose value should be between left and right
     *
     * @param field field in this condition
     * @param left  left part of the between condition
     * @param right right part of the between condition
     */
    protected BetweenCondition(String field, Object left, Object right) {
        this.field = field;
        this.left = left;
        this.right = right;
    }

    /**
     * This method will return a String of this condition
     *
     * @param parent this is the parent of this condition
     * @return a string
     */
    @Override
    public String toRecordString(Condition parent) {
        return "\"" + field + "\": { \"$between\": [" + (transformLeftRight()) + "]}";
    }

    private String transformLeftRight() {
        String result = (left instanceof String || left instanceof Character ? ("\"" + left + "\"") : left.toString());
        result += ",";

        return result + (right instanceof String || left instanceof Character ? ("\"" + right + "\"") : right.toString());
    }
}