package org.dynamicloud.api.criteria;

/**
 * This class represents an equal condition <b>left = right</b>
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/23/15
 **/
public class EqualCondition implements Condition {
    private char greaterLesser;
    private String left;
    private Object right;
    private boolean needQuotes;

    /**
     * This constructor will build an equal condition using left and right parts.
     *
     * @param left  left part of this equal condition
     * @param right right part of this equal condition
     */
    protected EqualCondition(String left, Object right, char greaterLesser) {
        this.left = left;
        this.right = right;
        this.needQuotes = right instanceof String || right instanceof Character;
        this.greaterLesser = greaterLesser;
    }

    /**
     * This method will return a String of this condition
     *
     * @param parent this is the parent of this condition
     * @return a json
     */
    @Override
    public String toRecordString(Condition parent) {
        if (greaterLesser == '-') {
            return "\"" + left + "\" : " + (needQuotes ? "\"" : "") + right.toString() + (needQuotes ? "\"" : "");
        }

        return "\"" + left + "\": { " + (greaterLesser == '>' ? "\"$gte\": " : "\"$lte\": ") +
                (needQuotes ? "\"" : "") + right.toString() + (needQuotes ? "\"" : "") +
                " }";
    }
}
