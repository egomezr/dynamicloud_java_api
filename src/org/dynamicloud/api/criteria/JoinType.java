package org.dynamicloud.api.criteria;

/**
 * This enum represents the different Join types
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 11/4/15
 **/
public enum JoinType {
    LEFT, RIGHT, INNER, LEFT_OUTER, RIGHT_OUTER;

    /**
     * This method returns the text according to this Join Type.
     *
     * @return the text according to this Join Type.
     */
    public String toString() {
        switch (this) {
            case LEFT:
                return "left";
            case RIGHT:
                return "right";
            case INNER:
                return "inner";
            case LEFT_OUTER:
                return "left outer";
            case RIGHT_OUTER:
                return "right outer";
            default:
                return "inner";
        }
    }
}