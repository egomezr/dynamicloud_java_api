package org.dynamicloud.api.criteria;

/**
 * This class represents a GroupBy clause.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/24/15
 **/
public class GroupByClause implements Condition {
    private String[] attributes;

    /**
     * Constructor to build a group by condition
     *
     * @param attributes attributes in group by clause
     */
    protected GroupByClause(String[] attributes) {
        this.attributes = attributes;
    }

    /**
     * Builds a GroupBy clause
     *
     * @param attributes in this group by clause.
     * @return a GroupBy clause
     */
    public static GroupByClause groupBy(String[] attributes) {
        return new GroupByClause(attributes);
    }

    /**
     * This method will return a String of this condition
     *
     * @param parent this is the parent of this condition
     * @return a string
     */
    @Override
    public String toRecordString(Condition parent) {
        String groupBy = "\"groupBy\": [";

        String attrs = "";
        for (String attr : attributes) {
            attrs += (attrs.length() == 0 ? "" : ",") + "\"" + attr + "\"";
        }

        return groupBy + attrs + "]";
    }
}
