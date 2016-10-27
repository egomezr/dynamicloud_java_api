package org.dynamicloud.api.criteria;

import org.dynamicloud.api.DynamiCloudUtil;
import org.dynamicloud.api.model.RecordModel;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents an exists condition to evaluate situations where a logical condition must be accomplished.
 * Example: Select those users where their user_ids exist in VIP model.
 * <p/>
 * Title: ExistsCondition.java<br>
 *
 * @author Eleazar Gomez (EEG)
 */
public class ExistsCondition implements Condition {
    private List<Condition> conditions = new LinkedList<Condition>();
    private RecordModel model;
    private String alias;
    private boolean not;
    private List<JoinClause> joins;

    /**
     * Default constructor
     */
    protected ExistsCondition() {
        this.joins = new LinkedList<JoinClause>();
    }

    /**
     * Builds an instance with a specific model an alias
     *
     * @param model RecordModel
     * @param alias alias to this model
     */
    protected ExistsCondition(RecordModel model, String alias, boolean not) {
        this.model = model;
        this.alias = alias;
        this.not = not;
        this.joins = new LinkedList<JoinClause>();
    }

    /**
     * This method will add a new condition to this ExistsCondition.
     *
     * @param condition new condition to a list of conditions to use
     * @return this instance of ExistsCondition
     */
    public ExistsCondition add(Condition condition) {
        conditions.add(condition);

        return this;
    }

    /**
     * Add a join to the list of joins
     *
     * @param join join clause
     * @return this instance of ExistsCondition
     */
    public ExistsCondition join(JoinClause join) {
        this.joins.add(join);

        return this;
    }

    /**
     * Sets the related alias to the model
     *
     * @param alias related alias to the model
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Sets the related model to this exists condition.
     * With this model, you can
     *
     * @param model related model
     */
    public void setModel(RecordModel model) {
        this.model = model;
    }

    /**
     * This method will return a String of this condition
     *
     * @param parent this is the parent of this condition
     * @return a json
     */
    @Override
    public String toRecordString(Condition parent) {

        String built = (not ? "\"$nexists\"" : "\"$exists\"") + ": { " + DynamiCloudUtil.buildJoinTag(joins) + ", " + (model == null ? "" : ("\"model\": " + model.getId() + ", ")) + (alias == null ? "" : ("\"alias\": \"" + alias + "\", ")) + "\"where\": {";

        if (conditions.size() > 0) {
            Condition global = conditions.get(0);
            if (conditions.size() > 1) {
                conditions = conditions.subList(1, conditions.size());
                for (Condition condition : conditions) {
                    global = new ANDCondition(global, condition);
                }
            }

            built += global.toRecordString(Conditions.ROOT);
        }

        return built + "}}";
    }
}