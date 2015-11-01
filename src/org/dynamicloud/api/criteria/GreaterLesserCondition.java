package org.dynamicloud.api.criteria;

/**
 * This class represents a condition that will use the following symbols <b>&gt; or &lt;</b>
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/23/15
 **/
public class GreaterLesserCondition implements Condition {
    private char greaterLesser;
    private String left;
    private Object right;
    private boolean needQuotes;

    /**
     *
     * @param greaterLesser indicates what kind of symbol will be used in this condition <b>&gt; or &lt;</b>.
     * @param left left part of this condition
     * @param right right part of this condition
     */
    public GreaterLesserCondition(char greaterLesser, String left, Object right) {
        this.greaterLesser = greaterLesser;
        this.left = left;
        this.right = right;
        this.needQuotes = right instanceof  String || right instanceof Character;
    }

    /**
     * This method will return a String of this condition
     * @param parent this is the parent of this condition
     * @return a json
     */
    @Override
    public String toRecordString(Condition parent) {
        return "\"" + left + "\": { " +
                (greaterLesser == '>' ? "\"$gt\"" : "\"$lt\"") + ": " +
                (needQuotes ? "\"" : "") + right.toString() + (needQuotes ? "\"" : "") +
                " }";
    }
}