package org.dynamicloud.api.criteria;

/**
 * This interface will represent a logical condition: AND, OR, LIKE, NOT LIKE, IN, NOT IN, EQUALS, GREATER THAN, GREATER EQUALS THAN
 * LESSER THAN, LESSER EQUALS THAN.
 *
 * With this interface it's available a way to build a query with conditions.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/22/15
 **/
public interface Condition {
    /**
     * This method will return a String of this condition
     * @param parent this is the parent of this condition
     * @return a string
     */
    String toRecordString(Condition parent);
}