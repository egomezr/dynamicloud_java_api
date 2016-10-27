package org.dynamicloud.api.criteria;

import org.dynamicloud.api.model.RecordModel;

/**
 * This is a builder to create conditions: AND, OR, LIKE, NOT LIKE, IN, NOT IN, EQUALS, GREATER THAN, GREATER EQUALS THAN
 * LESSER THAN, LESSER EQUALS THAN.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/22/15
 */
public class Conditions {

    public static final Condition ROOT = new Condition() {
        @Override
        public String toRecordString(Condition parent) {
            throw new UnsupportedOperationException("This is a root condition, this condition is used to start condition building process.");
        }
    };

    private static char WITHOUT = '-';

    /**
     * It will build an and condition using two parts (Left and Right)
     *
     * @param left  left part of and
     * @param right right part of and
     * @return A built condition
     */
    public static Condition and(Condition left, Condition right) {
        return new ANDCondition(left, right);
    }

    /**
     * It will build an or condition using two parts (Left and Right)
     *
     * @param left  left part of or
     * @param right right part of or
     * @return A built condition.
     */
    public static Condition or(Condition left, Condition right) {
        return new ORCondition(left, right);
    }

    /**
     * It will an in condition using an array of values.
     *
     * @param left   attribute to compare
     * @param values string values to build IN condition
     * @return a built condition.
     */
    public static Condition in(String left, String[] values) {
        return innerInCondition(left, values, false);
    }

    /**
     * It will an in condition using an array of values.
     *
     * @param left   attribute to compare
     * @param values number values to build IN condition
     * @return a built condition.
     */
    public static Condition in(String left, Number[] values) {
        return innerInCondition(left, values, false);
    }

    /**
     * It will an in condition using an array of values.
     *
     * @param left   attribute to compare
     * @param values character values to build IN condition
     * @return a built condition.
     */
    public static Condition in(String left, Character[] values) {
        return innerInCondition(left, values, false);
    }

    /**
     * It will an in condition using an array of values.
     *
     * @param left   attribute to compare
     * @param values string values to build IN condition
     * @return a built condition.
     */
    public static Condition notIn(String left, String[] values) {
        return innerInCondition(left, values, true);
    }

    /**
     * It will an in condition using an array of values.
     *
     * @param left   attribute to compare
     * @param values number values to build IN condition
     * @return a built condition.
     */
    public static Condition notIn(String left, Number[] values) {
        return innerInCondition(left, values, true);
    }

    /**
     * It will an in condition using an array of values.
     *
     * @param left   attribute to compare
     * @param values character values to build IN condition
     * @return a built condition.
     */
    public static Condition notIn(String left, Character[] values) {
        return innerInCondition(left, values, true);
    }

    /**
     * It will build a like condition.
     *
     * @param left attribute to comapare
     * @param like String to use for like condition
     * @return a built condition.
     */
    public static Condition like(String left, String like) {
        return new LikeCondition(left, like, false);
    }

    /**
     * It will build a not like condition.
     *
     * @param left attribute to comapare
     * @param like String to use for like condition
     * @return a built condition.
     */
    public static Condition notLike(String left, String like) {
        return new LikeCondition(left, like, true);
    }

    /**
     * It will build an equals condition.
     *
     * @param left  attribute to compare
     * @param right right part of this condition
     * @return a built condition.
     */
    public static Condition equals(String left, String right) {
        return innerEquals(left, right, WITHOUT);
    }

    /**
     * It will build a is null condition
     *
     * @param left attribute to compare
     * @return a built condition
     */
    public static Condition isNull(String left) {
        return new NullCondition(left, false);
    }

    /**
     * It will build a is null condition
     *
     * @param left attribute to compare
     * @return a built condition
     */
    public static Condition isNotNull(String left) {
        return new NullCondition(left, true);
    }

    /**
     * It will build an equals condition.
     *
     * @param left  attribute to compare
     * @param right Number to use for equals condition
     * @return a built condition.
     */
    public static Condition equals(String left, Number right) {
        return innerEquals(left, right, WITHOUT);
    }

    /**
     * It will build an equals condition.
     *
     * @param left  attribute to compare
     * @param right right part of this condition
     * @return a built condition.
     */
    public static Condition equals(String left, Character right) {
        return innerEquals(left, right, WITHOUT);
    }

    /**
     * It will build a not equals condition.
     *
     * @param left  attribute to compare
     * @param right right part of this condition
     * @return a built condition.
     */
    public static Condition notEquals(String left, Object right) {
        return innerNotEquals(left, right);
    }

    /**
     * It will build a greater equals condition.
     *
     * @param left  attribute to compare
     * @param right right part of this condition
     * @return a built condition.
     */
    public static Condition greaterEquals(String left, Object right) {
        return innerEquals(left, right, '>');
    }

    /**
     * It will build a greater than condition.
     *
     * @param left  attribute to compare
     * @param right right part of this condition
     * @return a built condition.
     */
    public static Condition greaterThan(String left, Object right) {
        return new GreaterLesserCondition('>', left, right);
    }

    /**
     * It will build a greater than condition.
     *
     * @param left  attribute to compare
     * @param right right part of this condition
     * @return a built condition.
     */
    public static Condition lesserThan(String left, Object right) {
        return new GreaterLesserCondition('<', left, right);
    }

    /**
     * It will build a lesser equals condition.
     *
     * @param left  attribute to compare
     * @param right right part of this condition
     * @return a built condition.
     */
    public static Condition lesserEquals(String left, Object right) {
        return innerEquals(left, right, '<');
    }

    /**
     * Builds a left join clause.
     *
     * @param model     target model of this join
     * @param alias     attached alias to this target model
     * @param Condition on condition of this join clause
     * @return a Join Clause as a condition
     */
    public static JoinClause leftJoin(RecordModel model, String alias, String Condition) {
        return new JoinClause(JoinType.LEFT, model, alias, Condition);
    }

    /**
     * Builds a left outer join clause.
     *
     * @param model     target model of this join
     * @param alias     attached alias to this target model
     * @param Condition on condition of this join clause
     * @return a Join Clause as a condition
     */
    public static JoinClause leftOuterJoin(RecordModel model, String alias, String Condition) {
        return new JoinClause(JoinType.LEFT_OUTER, model, alias, Condition);
    }

    /**
     * Builds a right join clause.
     *
     * @param model     target model of this join
     * @param alias     attached alias to this target model
     * @param Condition on condition of this join clause
     * @return a Join Clause as a condition
     */
    public static JoinClause rightJoin(RecordModel model, String alias, String Condition) {
        return new JoinClause(JoinType.RIGHT, model, alias, Condition);
    }

    /**
     * Builds a right outer join clause.
     *
     * @param model     target model of this join
     * @param alias     attached alias to this target model
     * @param Condition on condition of this join clause
     * @return a Join Clause as a condition
     */
    public static JoinClause rightOuterJoin(RecordModel model, String alias, String Condition) {
        return new JoinClause(JoinType.RIGHT_OUTER, model, alias, Condition);
    }

    /**
     * Builds a inner join clause.
     *
     * @param model     target model of this join
     * @param alias     attached alias to this target model
     * @param Condition on condition of this join clause
     * @return a Join Clause as a condition
     */
    public static JoinClause innerJoin(RecordModel model, String alias, String Condition) {
        return new JoinClause(JoinType.INNER, model, alias, Condition);
    }

    /**
     * Builds a between condition
     *
     * @param field field in this condition
     * @param left  left part of the between condition
     * @param right right part of the between condition
     * @return a new instance of BetweenCondition
     */
    public static Condition between(String field, Object left, Object right) {
        return new BetweenCondition(field, left, right);
    }

    /**
     * Creates a new instance of ExistsCondition
     *
     * @return a new instance of ExistsCondition
     */
    public static ExistsCondition exists() {
        return new ExistsCondition(null, null, false);
    }

    /**
     * Creates a new instance of ExistsCondition
     *
     * @param model record model
     * @param alias alias to this model (optional)
     * @return a new instance of ExistsCondition
     */
    public static ExistsCondition exists(RecordModel model, String alias) {
        return new ExistsCondition(model, alias, false);
    }

    /**
     * Creates a new instance of ExistsCondition
     *
     * @param model record model
     * @return a new instance of ExistsCondition
     */
    public static ExistsCondition exists(RecordModel model) {
        return new ExistsCondition(model, null, false);
    }

    /**
     * Creates a new instance of ExistsCondition
     *
     * @return a new existance of ExistsCondition
     */
    public static ExistsCondition notExists() {
        return new ExistsCondition(null, null, true);
    }

    /**
     * Creates a new instance of ExistsCondition
     *
     * @param model record model
     * @param alias alias to this model (optional)
     * @return a new instance of ExistsCondition
     */
    public static ExistsCondition notExists(RecordModel model, String alias) {
        return new ExistsCondition(model, alias, true);
    }

    /**
     * Creates a new instance of ExistsCondition
     *
     * @param model record model
     * @return a new instance of ExistsCondition
     */
    public static ExistsCondition notExists(RecordModel model) {
        return new ExistsCondition(model, null, true);
    }

    /**
     * This method will build a not equals condition.
     *
     * @param left  value to compare
     * @param right right part of this condition
     * @return a built condition
     */
    private static Condition innerNotEquals(String left, Object right) {
        return new NotEqualCondition(left, right);
    }

    /**
     * This method will build either a equals condition.
     *
     * @param left          value to compare
     * @param greaterLesser indicates if greater or lesser condition must be added.
     * @return a built condition
     */
    private static Condition innerEquals(String left, Object right, char greaterLesser) {
        return new EqualCondition(left, right, greaterLesser);
    }

    /**
     * It will either an in or not in condition using an array of values and a boolean that indicates
     * what kind of IN will be built.
     *
     * @param left   attribute to compare
     * @param values String values to build IN condition
     * @return a built condition.
     */
    private static Condition innerInCondition(String left, Object[] values, boolean notIn) {
        return new INCondition(left, values, notIn);
    }
}