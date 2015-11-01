package org.dynamicloud.api.criteria;

/**
 * This class represents an OrderBy clause
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/24/15
 **/
public class OrderByClause implements Condition {
    private boolean asc;
    private String attribute;

    /**
     * Build an orderBy clause using desc flag
     * @param attribute attribute to use to order
     *
     * @return an orderBy object
     */
    public static OrderByClause desc(String attribute) {
        OrderByClause order = new OrderByClause();
        order.asc = false;
        order.attribute = attribute;

        return order;
    }

    /**
     * Build an orderBy clause using asc flag
     * @param attribute attribute to use to order
     *
     * @return an orderBy object
     */
    public static OrderByClause asc(String attribute) {
        OrderByClause order = new OrderByClause();
        order.asc = true;
        order.attribute = attribute;

        return order;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * This method will return a String of this condition
     *
     * @param parent this is the parent of this condition
     * @return a string
     */
    @Override
    public String toRecordString(Condition parent) {
        return "\"order\": \"" + attribute + (asc ? " ASC" : " DESC") + "\"";
    }
}