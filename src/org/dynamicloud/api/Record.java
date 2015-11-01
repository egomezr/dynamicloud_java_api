package org.dynamicloud.api;

/**
 * This interface declared
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/24/15
 **/
public interface Record {
    /**
     * get the value paired with attribute
     * @param attribute attribute to use
     *
     * @return the value paired with attribute
     */
    String getValue(String attribute);

    /**
     * get the number value paired with attribute
     * @param attribute attribute to use
     *
     * @return the number value paired with attribute
     */
    Number getNumberValue(String attribute);

    /**
     * get the values paired with attribute
     * @param attribute attribute to use
     *
     * @return the values paired with attribute
     */
    String[] getValues(String attribute);

    /**
     * Adds a new value paired with attribute
     *
     * @param attribute attribute to be paired
     * @param value value
     */
    void addValue(String attribute, String value);

    /**
     * Adds a new number value paired with attribute
     *
     * @param attribute attribute to be paired
     * @param value number value
     */
    void addValue(String attribute, Number value);

    /**
     * Adds a new values paired with attribute
     *
     * @param attribute attribute to be paired
     * @param values values
     */
    void addValue(String attribute, String [] values);
}