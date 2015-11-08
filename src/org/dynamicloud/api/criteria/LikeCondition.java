package org.dynamicloud.api.criteria;

/**
 * This class represents a like condition <b>left like '%som%thing%'</b>
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/23/15
 **/
public class LikeCondition implements Condition {
    private String left;
    private String right;
    private boolean notLike;

    /**
     * This constructor will build a like condition using left and right part.
     *
     * @param left  left part of this like condition.
     * @param right right part of this like condition.
     */
    protected LikeCondition(String left, String right, boolean notLike) {
        this.left = left;
        this.right = right;
        this.notLike = notLike;
    }

    /**
     * This method will return a String of this condition
     *
     * @param parent this is the parent of this condition
     * @return a json
     */
    @Override
    public String toRecordString(Condition parent) {
        return "\"" + left + "\": { \"$"+ (notLike ? "n" : "")+"like\" : " + "\"" + right + "\"" + " }";
    }
}