package org.dynamicloud.api;

import org.dynamicloud.api.criteria.Condition;
import org.dynamicloud.api.criteria.JoinClause;
import org.dynamicloud.api.model.RecordModel;
import org.dynamicloud.exception.DynamicloudProviderException;

import java.util.List;

/**
 * This interface declares the accessible methods to execute operations.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/29/15
 **/
public interface Query<T> {

    /**
     * Apply a desc ordering to the current order by object
     * An IllegalStateException will be thrown if orderBy object is null
     *
     * @return this instance of Query
     */
    Query<T> desc();

    /**
     * Apply a asc ordering to the current order by object
     * An IllegalStateException will be thrown if orderBy object is null
     *
     * @return this instance of Query
     */
    Query<T> asc();

    /**
     * Attaches a alias to this query, the model in this query will use this alias in Join Clauses or whatever situation where alias is needed.
     *
     * @param alias alias to attach
     * @return this instance of Query
     */
    Query<T> setAlias(String alias);

    /**
     * This method will add a new condition to an AND list of conditions.
     *
     * @param condition new condition to a list of conditions to use
     * @return this instance of Query
     */
    Query<T> add(Condition condition);

    /**
     * This method sets the projection to use in this query.  The query execution will return those projection.
     * If projection == null then, this query will returns all model's projection.
     *
     * @param projection projection in this query
     * @return this instance of Query
     */
    Query<T> setProjection(String[] projection);

    /**
     * This method sets the projection to use in this query
     * Additionally, the projection could be math operations as well: COUNT, MAX, MIN, SUM, AVG, etc.
     *
     * @param projection projection in this query
     * @return this instance of Query
     */
    Query<T> setProjection(String projection);

    /**
     * Sets an offset to this query to indicates the page of a big data result.
     *
     * @param offset new offset
     * @return this instance of Query
     */
    Query<T> setOffset(int offset);

    /**
     * Sets how many items per page (offset) this query will fetch
     *
     * @param count how many items
     * @return this instance of Query
     */
    Query<T> setCount(int count);

    /**
     * This method will execute a query and returns a list of records
     *
     * @throws DynamicloudProviderException if any error occurs.
     */
    RecordResults<T> list() throws DynamicloudProviderException;

    /**
     * This method adds an order by condition.  The condition will have an asc ordering by default.
     *
     * @param attribute attribute by the query will be ordered.
     * @return this instance of Query
     */
    Query<T> orderBy(String attribute);

    /**
     * This method create a groupBy condition using attribute
     *
     * @param attribute attribute by this query will group.
     * @return this instance of Query
     */
    Query<T> groupBy(String attribute);

    /**
     * get the current conditions
     *
     * @return the conditions
     */
    List<Condition> getConditions();

    /**
     * This method create a groupBy condition using attributes
     *
     * @param attributes attribute by this query will group.
     * @return this instance of Query
     */
    Query<T> groupBy(String[] attributes);

    /**
     * Add a join to the list of joins
     *
     * @param join join clause
     * @return this instance of Query
     */
    Query<T> join(JoinClause join);

    /**
     * Gets the current offset so far.  This attribute will increase according calls of method next()
     *
     * @return int of current offset
     */
    int getCurrentOffSet();

    /**
     * Will execute a list operation with an offset += count and will use the same callback object in list method.
     * If list() method without callback was called, then this method will return a RecordResults object otherwise null
     *
     * @throws DynamicloudProviderException if any error occurs.
     */
    RecordResults<T> next() throws DynamicloudProviderException;

    /**
     * Returns the current RecordModel associated to this query
     *
     * @return RecordModel
     */
    RecordModel getModel();
}