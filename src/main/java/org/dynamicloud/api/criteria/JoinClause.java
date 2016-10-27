package org.dynamicloud.api.criteria;

import org.dynamicloud.api.model.RecordModel;

/**
 * This class represents a Join clause
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 11/4/15
 **/
public class JoinClause implements Condition {
    /**
     * JoinType.LEFT, JoinType.RIGHT, JoinType.FULL, JoinType.INNER
     */
    private JoinType joinType;
    /**
     * Target model
     */
    private RecordModel model;

    /**
     * Join condition
     */
    private String joinCondition;

    /**
     * Alias to target model
     */
    private String alias;

    /**
     * Builds a JoinClause using type, model and compatible condition.
     *
     * @param joinType      join type
     * @param model         target model
     * @param alias         alias to use with this target model.  You don't need to concatenate the alias in join condition.
     * @param joinCondition compatible join condition
     */
    protected JoinClause(JoinType joinType, RecordModel model, String alias, String joinCondition) {
        this.joinType = joinType;
        this.model = model;
        this.joinCondition = joinCondition;
        this.alias = alias;
    }

    /**
     * This method will return a String of this condition
     *
     * @param parent this is the parent of this condition
     * @return a string
     */
    @Override
    public String toRecordString(Condition parent) {
        return "{ \"type\": \"" + joinType + "\", \"alias\": \"" + alias + "\", \"target\": \"" + model.getId() + "\", \"on\": \"" + joinCondition + "\" }";
    }
}