package org.dynamicloud.api.criteria;

/**
 * This class represents an IN and NOT IN condition.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/24/15
 **/
public class INCondition implements Condition {
    private String left;
    private Object[] values;
    private boolean notIn;

    /**
     * Constructor to build either IN or NOT IN condition
     *
     * @param left   attribute to compare
     * @param values values to use to build IN or NOT IN condition
     * @param notIn  indicates if this condition is a not in.
     */
    public INCondition(String left, Object[] values, boolean notIn) {
        this.left = left;
        this.values = values;
        this.notIn = notIn;
    }

    /**
     * This method will return a String of this condition
     *
     * @param parent this is the parent of this condition
     * @return a string
     */
    @Override
    public String toRecordString(Condition parent) {
        String condition = "\"" + left + "\": {" + (notIn ? "\"$nin\"" : "\"$in\"") + ": [";

        String items = "";
        for (Object value : values) {
            items += (items.length() == 0 ? "" : ",") + (value instanceof String || value instanceof Character ? "\"" : "") + value + (value instanceof String || value instanceof Character ? "\"" : "");
        }

        return condition + items + "]}";
    }
}