package org.dynamicloud.junit.tests;

import junit.framework.TestCase;
import org.dynamicloud.api.DynamiCloudUtil;
import org.dynamicloud.api.RecordResults;
import org.dynamicloud.api.criteria.*;
import org.dynamicloud.junit.bean.ModelFields;
import org.dynamicloud.service.ServiceResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 10/16/15
 **/
public class TestConditionClause extends TestCase {
    public void testEqualCondition() {
        Condition condition = new EqualCondition("age", 32, '<');
        assertEquals("\"age\": { \"$lte\": 32 }", condition.toRecordString(Conditions.ROOT));

        condition = new GreaterLesserCondition('<', "age", 32);
        assertEquals("\"age\": { \"$lt\": 32 }", condition.toRecordString(Conditions.ROOT));

        condition = new GreaterLesserCondition('>', "age", 32);
        assertEquals("\"age\": { \"$gt\": 32 }", condition.toRecordString(Conditions.ROOT));

        condition = new EqualCondition("age", 32, '>');
        assertEquals("\"age\": { \"$gte\": 32 }", condition.toRecordString(Conditions.ROOT));

        condition = new EqualCondition("age", "32", '>');
        assertEquals("\"age\": { \"$gte\": \"32\" }", condition.toRecordString(Conditions.ROOT));

        condition = new EqualCondition("age", 32, '-');
        assertEquals("\"age\" : 32", condition.toRecordString(Conditions.ROOT));

        condition = new EqualCondition("age", "32", '-');
        assertEquals("\"age\" : \"32\"", condition.toRecordString(Conditions.ROOT));
    }

    public void testInCondition() {
        Condition condition = new INCondition("age", new Integer[]{1, 2, 3}, false);
        assertEquals("\"age\": {\"$in\": [1,2,3]}", condition.toRecordString(Conditions.ROOT));

        condition = new INCondition("age", new String[]{"1", "2", "3"}, false);
        assertEquals("\"age\": {\"$in\": [\"1\",\"2\",\"3\"]}", condition.toRecordString(Conditions.ROOT));

        condition = new INCondition("age", new Integer[]{1, 2, 3}, true);
        assertEquals("\"age\": {\"$nin\": [1,2,3]}", condition.toRecordString(Conditions.ROOT));

        condition = new INCondition("age", new String[]{"1", "2", "3"}, true);
        assertEquals("\"age\": {\"$nin\": [\"1\",\"2\",\"3\"]}", condition.toRecordString(Conditions.ROOT));
    }

    public void testLikeCondition() {
        Condition condition = new LikeCondition("name", "%eleazar%", false);
        assertEquals("\"name\": { \"$like\" : \"%eleazar%\" }", condition.toRecordString(Conditions.ROOT));

        condition = new LikeCondition("name", "%eleazar%", true);
        assertEquals("\"name\": { \"$nlike\" : \"%eleazar%\" }", condition.toRecordString(Conditions.ROOT));
    }

    public void testNotEqualCondition() {
        Condition condition = new NotEqualCondition("name", "eleazar");
        assertEquals("\"$ne\" : {\"name\" : \"eleazar\"}", condition.toRecordString(Conditions.ROOT));

        condition = new NotEqualCondition("age", 4);
        assertEquals("\"$ne\" : {\"age\" : 4}", condition.toRecordString(Conditions.ROOT));
    }

    public void testNullCondition() {
        Condition condition = new NullCondition("name", false);
        assertEquals("\"name\": {\"$null\": \"1\"}", condition.toRecordString(Conditions.ROOT));

        condition = new NullCondition("name", true);
        assertEquals("\"name\": {\"$notNull\": \"1\"}", condition.toRecordString(Conditions.ROOT));
    }

    public void testOrCondition() {
        Condition condition = new ORCondition(new NullCondition("name", false), new NullCondition("name", true));
        assertEquals("\"$or\": {\"name\": {\"$null\": \"1\"},\"name\": {\"$notNull\": \"1\"}}", condition.toRecordString(Conditions.ROOT));

        condition = new ORCondition(new NullCondition("name", false), new LikeCondition("name", "%eleazar%", false));
        assertEquals("\"$or\": {\"name\": {\"$null\": \"1\"},\"name\": { \"$like\" : \"%eleazar%\" }}", condition.toRecordString(Conditions.ROOT));
    }

    public void testAndCondition() {
        Condition condition = new ANDCondition(new NullCondition("name", false), new NullCondition("name", true));
        assertEquals("\"name\": {\"$null\": \"1\"},\"name\": {\"$notNull\": \"1\"}", condition.toRecordString(Conditions.ROOT));

        condition = new ANDCondition(new NullCondition("name", false), new LikeCondition("name", "%eleazar%", false));
        assertEquals("\"name\": {\"$null\": \"1\"},\"name\": { \"$like\" : \"%eleazar%\" }", condition.toRecordString(Conditions.ROOT));
    }

    public void testGroupByClause() {
        GroupByClause clause = new GroupByClause(new String[]{"name", "age"});
        assertEquals("\"groupBy\": [\"name\",\"age\"]", clause.toRecordString(Conditions.ROOT));
    }

    public void testOrderByClause() {
        OrderByClause clause = new OrderByClause();
        clause.setAttribute("name");
        clause.setAsc(true);

        assertEquals("name", clause.getAttribute());
        assertEquals(true, clause.isAsc());

        assertEquals("\"order\": \"name ASC\"", clause.toRecordString(Conditions.ROOT));

        clause.setAttribute("name");
        clause.setAsc(false);

        assertEquals("name", clause.getAttribute());
        assertEquals(false, clause.isAsc());

        assertEquals("\"order\": \"name DESC\"", clause.toRecordString(Conditions.ROOT));
    }

    public void testDynamicUtil() {
        ModelFields instance = new ModelFields();
        instance.setName("Eleazar");

        assertEquals("{\"name\":\"Eleazar\"}", DynamiCloudUtil.buildFieldsJSON(instance));

        instance.setEmail("ego@dynamicloud.org");
        assertEquals("{\"name\":\"Eleazar\",\"email\":\"ego@dynamicloud.org\"}", DynamiCloudUtil.buildFieldsJSON(instance));

        String[] citieArray = {"sp", "rj", "bs"};
        instance.setCities(citieArray);
        assertEquals("{\"name\":\"Eleazar\",\"email\":\"ego@dynamicloud.org\",\"cities\":\"sp,rj,bs\"}",
                DynamiCloudUtil.buildFieldsJSON(instance));

        instance.setCities(new String[]{"sp", "rj", null, "bs", null});
        assertEquals("{\"name\":\"Eleazar\",\"email\":\"ego@dynamicloud.org\",\"cities\":\"sp,rj,bs\"}",
                DynamiCloudUtil.buildFieldsJSON(instance));

        try {
            JSONObject data = new JSONObject();
            JSONArray records = new JSONArray();
            data.put("records", records);
            JSONObject record = new JSONObject();
            record.put("name", "Eleazar");
            record.put("email", "ego@dynamicloud.org");

            records.put(record);

            JSONObject cities = new JSONObject();
            JSONArray array = new JSONArray();
            array.put("sp");
            array.put("rj");
            array.put("bs");
            cities.put("value", array);

            record.put("cities", cities);

            List<Object> recordList = DynamiCloudUtil.getRecordList(data, ModelFields.class);
            assertEquals("Eleazar", ((ModelFields) recordList.get(0)).getName());
            assertEquals("ego@dynamicloud.org", ((ModelFields) recordList.get(0)).getEmail());
            assertEquals(citieArray[0], ((ModelFields) recordList.get(0)).getCities()[0]);
            assertEquals(citieArray[1], ((ModelFields) recordList.get(0)).getCities()[1]);
            assertEquals(citieArray[2], ((ModelFields) recordList.get(0)).getCities()[2]);
        } catch (JSONException | IllegalAccessException | InstantiationException e) {
            fail(e.getMessage());
        }

        try {
            String json = "{\"records\": {\"total\": 12, \"size\": 3, \"records\": [{\"name\": \"eleazar\"}, {\"name\": \"enrique\"}, {\"name\": \"eleana\"}]}}";
            RecordResults recordResults = DynamiCloudUtil.buildRecordResults(new ServiceResponse(json), ModelFields.class);
            assertEquals(3, recordResults.getFastReturnedSize());
            assertEquals(12, recordResults.getTotalRecords());
            assertFalse(recordResults.getRecords() == null);
            assertEquals(3, recordResults.getRecords().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}