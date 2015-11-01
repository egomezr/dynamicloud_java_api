package org.dynamicloud.api;

import org.dynamicloud.api.annotation.Bind;
import org.dynamicloud.api.criteria.*;
import org.dynamicloud.api.model.RecordFieldItem;
import org.dynamicloud.exception.DynamicloudProviderException;
import org.dynamicloud.logger.LoggerTool;
import org.dynamicloud.service.ServiceResponse;
import org.dynamicloud.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a class with utility methods
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/26/15
 **/
public class DynamiCloudUtil {
    public static final String SET = "set";
    public static final String RECORDS = "records";
    public static final String TOTAL = "total";
    public static final String SIZE = "size";
    public static final String GET = "get";
    private static final LoggerTool logger = LoggerTool.getLogger(DynamiCloudUtil.class);

    /**
     * This utility will build a RecordResults object
     *
     * @param response   ServiceResponse from Dynamicloud servers
     * @param boundClass it is the class bound with fields in Dynamicloud.
     * @return RecordResults object
     */
    public static RecordResults buildRecordResults(ServiceResponse response, Class boundClass) {
        RecordResults<Object> results = new RecordResults<>();
        try {

            JSONObject json = new JSONObject(response.getResponse());

            JSONObject data = json.getJSONObject(RECORDS);

            results.setTotalRecords(data.getInt(TOTAL));
            results.setFastReturnedSize(data.getInt(SIZE));

            if (boundClass == null) {
                results.setRecords(getRecordList(data));
            } else {
                results.setRecords(getRecordList(data, boundClass));
            }

        } catch (Exception ignore) {
            logger.error("General error", ignore);
        }

        return results;
    }

    /**
     * This method will extract data and Bind each field with attributes in mapper:getInstance method instance
     *
     * @param data       json with all data from Dynamicloud servers
     * @param boundClass it is the class bound with fields in Dynamicloud.
     * @return list of records
     */
    public static List<Object> getRecordList(JSONObject data, Class boundClass)
            throws JSONException, IllegalAccessException, InstantiationException {
        List<Object> recordsList = new LinkedList<>();

        JSONArray records = data.getJSONArray(RECORDS);
        for (int i = 0; i < records.length(); i++) {
            Object r = boundClass.newInstance();

            JSONObject jr = records.getJSONObject(i);
            setData2record(r, jr);

            recordsList.add(r);
        }

        return recordsList;
    }

    /**
     * Fill the record object r with data from jr JSON object
     *
     * @param r  record object
     * @param jr JSON object
     */
    protected static void setData2record(Object r, JSONObject jr) {
        Iterator keys = jr.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object value = jr.get(key);
            if (value instanceof String) {
                setValueToRecord(r, key, (String) value);
            } else if (value instanceof Number) {
                setValueToRecord(r, key, (Number) value);
            } else if (value instanceof JSONObject) {
                JSONObject jv = (JSONObject) value;

                String k = (String) jv.keys().next();
                Object v = jv.get(k);
                if (v instanceof JSONArray) {
                    JSONArray array = (JSONArray) v;
                    String[] values = new String[array.length()];
                    for (int j = 0; j < array.length(); j++) {
                        values[j] = array.get(j).toString();
                    }

                    setValuesToRecord(r, key, values);
                } else if (v instanceof String) {
                    setValueToRecord(r, key, (String) v);
                }
            }
        }
    }

    /**
     * Sets a value this object using the key param to match the attribute in object r
     *
     * @param r     object r
     * @param key   key to match with attribute in object r
     * @param value value to use
     */
    protected static void setValueToRecord(Object r, String key, String value) {
        try {
            Method method = getMethod(r, key);
            if (method == null) {
                logger.warn("Bound method with " + key + " wasn't found.");
            } else {
                method.invoke(r, value);
            }
        } catch (Exception e) {
            logger.warn("Bound method with " + key + " wasn't found cause this error (" + e.getMessage() + ").");
        }
    }

    /**
     * Sets a value this object using the key param to match the attribute in object r
     *
     * @param r     object r
     * @param key   key to match with attribute in object r
     * @param value value to use
     */
    protected static void setValueToRecord(Object r, String key, Number value) {
        try {
            Method method = getMethod(r, key);
            if (method == null) {
                logger.warn("Bound method with " + key + " wasn't found.");
            } else {
                if (!setNumberToRecord(method, r, value)) {
                    if (!setNumberToRecord(method, r, value.byteValue())) {
                        if (!setNumberToRecord(method, r, value.intValue())) {
                            if (!setNumberToRecord(method, r, value.longValue())) {
                                if (!setNumberToRecord(method, r, value.floatValue())) {
                                    if (!setNumberToRecord(method, r, value.doubleValue())) {
                                        logger.warn("Bound method with this kind of object '" + value.getClass().getName() + "' wasn't found.");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Bound method with " + key + " wasn't found cause this error (" + e.getMessage() + ").");
        }
    }

    /**
     * Will try to set the number value to this method.
     *
     * @param method method that will be invoked to set the number value.
     * @param r      record object
     * @param value  number value
     * @return will return true if everything i'ts ok false otherwise.
     */
    private static boolean setNumberToRecord(Method method, Object r, Number value) {
        try {
            method.invoke(r, value);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Sets a values this object using the key param to match the attribute in object r
     *
     * @param r      object r
     * @param key    key to match with attribute in object r
     * @param values values to use
     */
    protected static void setValuesToRecord(Object r, String key, String[] values) {
        try {
            Method method = getMethod(r, key);
            if (method == null) {
                logger.warn("Bound method with " + key + " wasn't found.");
            } else {
                method.invoke(r, new Object[]{values});
            }
        } catch (Exception e) {
            logger.warn("Bound method with " + key + " wasn't found cause this error (" + e.getMessage() + ").");
        }
    }

    /**
     * This method will find a declared method with annotation Bind.
     * If this method did not find a method then will return null
     *
     * @param r      object to use and find the specific method
     * @param target bound string to find method.
     * @return the specific method.
     */
    private static Method getMethod(Object r, String target) {
        Method[] methods = r.getClass().getMethods();
        for (Method m : methods) {
            Bind bind = m.getAnnotation(Bind.class);
            if (bind != null && bind.field().equals(target)) {
                return m;
            }
        }

        return null;
    }

    /**
     * This method will extract data and return a list of records
     *
     * @param data json with all data from Dynamicloud servers
     * @return list of records
     */
    private static List<Object> getRecordList(JSONObject data) throws JSONException {
        List<Object> recordsList = new LinkedList<>();

        JSONArray records = data.getJSONArray(RECORDS);
        for (int i = 0; i < records.length(); i++) {
            Record r = new RecordImpl();

            JSONObject jr = records.getJSONObject(i);
            Iterator keys = jr.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Object value = jr.get(key);
                if (value instanceof String) {
                    r.addValue(key, (String) value);
                } else if (value instanceof Number) {
                    r.addValue(key, (Number) value);
                } else if (value instanceof JSONObject) {
                    JSONObject jv = (JSONObject) value;

                    String k = (String) jv.keys().next();
                    Object v = jv.get(k);
                    if (v instanceof JSONArray) {
                        JSONArray array = (JSONArray) v;
                        String[] values = new String[array.length()];
                        for (int j = 0; j < array.length(); j++) {
                            values[j] = array.get(j).toString();
                        }

                        r.addValue(key, values);
                    } else if (v instanceof String) {
                        r.addValue(key, (String) v);
                    }
                }
            }

            recordsList.add(r);
        }
        return recordsList;
    }

    /**
     * Builds a compatible string to update a record.
     * This method will find the Bind annotation to get field name and its value form bound object.
     *
     * @param instance Object where data is extracted
     * @return compatible string
     */
    public static String buildFieldsJSON(BoundInstance instance) {
        JSONObject json = new JSONObject();

        Method[] methods = instance.getClass().getMethods();
        for (Method m : methods) {
            Bind a = m.getAnnotation(Bind.class);
            if (a != null) {
                String fieldName = a.field();
                String methodName = GET + m.getName().replaceAll(SET, StringUtils.EMPTY);

                try {
                    Method getMethod = instance.getClass().getMethod(methodName);
                    Object result = getMethod.invoke(instance);
                    if (result instanceof Object[]) {
                        String array = "";
                        for (Object o : (Object[]) result) {
                            if (o != null) {
                                array += (array.length() == 0 ? "" : ",") + o.toString();
                            }
                        }

                        json.put(fieldName, array);
                    } else if (result != null) {
                        json.put(fieldName, result.toString());
                    }
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                }
            }
        }

        return json.toString();
    }

    /**
     * Builds an array of RecordFieldItems according JSONArray
     *
     * @param array JSONArray with pair value, text.
     * @return array of RecordFieldItems
     */
    public static RecordFieldItem[] buildItems(JSONArray array) {
        if (array.length() > 0) {
            RecordFieldItem[] items = new RecordFieldItem[array.length()];

            for (int i = 0; i < array.length(); i++) {
                JSONObject item = getItemJsonObject(array, i);

                RecordFieldItem ri = getRecordFieldItem(item);

                if (ri != null) {
                    items[i] = ri;
                }
            }

            return items;
        }

        return new RecordFieldItem[]{};
    }

    /**
     * Returns an object RecordFieldItem using JSONObject
     *
     * @param item JSONObject with pair text,value
     * @return RecordFieldItem
     */
    private static RecordFieldItem getRecordFieldItem(JSONObject item) {
        try {
            RecordFieldItem ri = new RecordFieldItem();
            ri.setText(item.getString("text"));
            ri.setValue(item.getString("value"));

            return ri;
        } catch (Exception ignore) {
            //Ignore
        }

        return null;
    }

    /**
     * Gets JSONObject at index i
     *
     * @param array array with JSONObjects
     * @param i     index
     * @return JSONObject with pair text,value
     */
    private static JSONObject getItemJsonObject(JSONArray array, int i) {
        JSONObject item = null;
        try {
            item = array.getJSONObject(i);
        } catch (JSONException ignore) {

        }
        return item;
    }

    /**
     * Builds a compatible String to use in service executions
     *
     * @return compatible String
     * @throws DynamicloudProviderException if any error occurs
     */
    public static String buildString(List<Condition> conditions, GroupByClause groupBy, OrderByClause orderBy,
                                     String projection) throws DynamicloudProviderException {

        String built = "{" + (projection == null ? "" : projection) + "\"where\": {";

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

        built += "}";

        if (groupBy != null) {
            built += "," + groupBy.toRecordString(Conditions.ROOT);
        }

        if (orderBy != null) {
            built += "," + orderBy.toRecordString(Conditions.ROOT);
        }

        return built + "}";
    }

    /**
     * Build a compatible string using projection
     *
     * @return string using projection
     */
    public static String buildProjection(String[] projection) {
        if (projection == null || projection.length == 0) {
            return "";
        }

        String columns = "\"columns\": [";
        String cols = "";
        for (String field : projection) {
            cols += (cols.length() == 0 ? "" : ", ") + "\"" + field + "\"";
        }

        return columns + cols + "], ";
    }


}