package org.dynamicloud.api.criteria;

/**
 * This class represents a not equal condition <b>left != '%som%thing%'</b>
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/24/15
 **/
public class NotEqualCondition implements Condition {
    private String left;
    private Object right;

    /**
     * Constructor that builds this condition
     *
     * @param left  attribute to compare
     * @param right right part of this condition
     */
    protected NotEqualCondition(String left, Object right) {
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
        return "\"$ne\" : {\"" + left + "\" : " + (right instanceof String || right instanceof Character ? "\"" : "") + right + (right instanceof String || right instanceof Character ? "\"" : "") + "}";
    }
}
