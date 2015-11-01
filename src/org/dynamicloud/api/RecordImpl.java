package org.dynamicloud.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a record in Dynamicloud
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/24/15
 **/
public class RecordImpl implements Record {
    private Map<String, Object> map = new HashMap<>();

    /**
     * gets the value paired with attribute
     *
     * @param attribute attribute to use
     * @return the value paired with attribute
     */
    @Override
    public String getValue(String attribute) {
        Object obj = map.get(attribute);
        if (obj instanceof String) {
            return (String) obj;
        }

        throw new IllegalStateException("The attribute " + attribute + " doesn't have a paired string.");
    }

    /**
     * get the number value paired with attribute
     *
     * @param attribute attribute to use
     * @return the number value paired with attribute
     */
    @Override
    public Number getNumberValue(String attribute) {
        return (Number) map.get(attribute);
    }

    /**
     * get the values paired with attribute
     *
     * @param attribute attribute to use
     * @return the values paired with attribute
     */
    @Override
    public String[] getValues(String attribute) {
        Object obj = map.get(attribute);
        if (obj instanceof String[]) {
            return (String[]) obj;
        }

        throw new IllegalStateException("The attribute " + attribute + " doesn't have a paired string array.");
    }

    /**
     * Adds a new value paired with attribute
     *
     * @param attribute attribute to be paired
     * @param value     value
     */
    @Override
    public void addValue(String attribute, String value) {
        map.put(attribute, value);
    }

    /**
     * Adds a new number value paired with attribute
     *
     * @param attribute attribute to be paired
     * @param value     number value
     */
    @Override
    public void addValue(String attribute, Number value) {
        map.put(attribute, value);
    }

    /**
     * Adds a new values paired with attribute
     *
     * @param attribute attribute to be paired
     * @param values    values
     */
    @Override
    public void addValue(String attribute, String[] values) {
        map.put(attribute, values);
    }

    @Override
    public String toString() {
        return "RecordImpl{" +
                "map=" + map +
                '}';
    }
}