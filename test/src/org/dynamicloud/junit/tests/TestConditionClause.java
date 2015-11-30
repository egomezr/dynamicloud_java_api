package org.dynamicloud.junit.tests;

import junit.framework.TestCase;
import org.dynamicloud.api.DynamiCloudUtil;
import org.dynamicloud.api.RecordResults;
import org.dynamicloud.api.criteria.*;
import org.dynamicloud.api.model.RecordModel;
import org.dynamicloud.junit.bean.ModelFields;
import org.dynamicloud.service.ServiceResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 10/16/15
 **/
public class TestConditionClause extends TestCase {
    public void testEqualCondition() {
        Condition condition = Conditions.lesserEquals("age", 32);
        assertEquals("\"age\": { \"$lte\": 32 }", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.lesserThan("age", 32);
        assertEquals("\"age\": { \"$lt\": 32 }", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.greaterThan("age", 32);
        assertEquals("\"age\": { \"$gt\": 32 }", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.greaterEquals("age", 32);
        assertEquals("\"age\": { \"$gte\": 32 }", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.greaterEquals("age", "32");
        assertEquals("\"age\": { \"$gte\": \"32\" }", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.equals("age", 32);
        assertEquals("\"age\" : 32", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.equals("age", "32");
        assertEquals("\"age\" : \"32\"", condition.toRecordString(Conditions.ROOT));
    }

    public void testInCondition() {
        Condition condition = Conditions.in("age", new Integer[]{1, 2, 3});
        assertEquals("\"age\": {\"$in\": [1,2,3]}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.in("age", new String[]{"1", "2", "3"});
        assertEquals("\"age\": {\"$in\": [\"1\",\"2\",\"3\"]}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.notIn("age", new Integer[]{1, 2, 3});
        assertEquals("\"age\": {\"$nin\": [1,2,3]}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.notIn("age", new String[]{"1", "2", "3"});
        assertEquals("\"age\": {\"$nin\": [\"1\",\"2\",\"3\"]}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.notIn("age", new Character[]{'1', '2', '3'});
        assertEquals("\"age\": {\"$nin\": [\"1\",\"2\",\"3\"]}", condition.toRecordString(Conditions.ROOT));
    }

    public void testLikeCondition() {
        Condition condition = Conditions.like("name", "%eleazar%");
        assertEquals("\"name\": { \"$like\" : \"%eleazar%\" }", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.notLike("name", "%eleazar%");
        assertEquals("\"name\": { \"$nlike\" : \"%eleazar%\" }", condition.toRecordString(Conditions.ROOT));
    }

    public void testNotEqualCondition() {
        Condition condition = Conditions.notEquals("name", "eleazar");
        assertEquals("\"$ne\" : {\"name\" : \"eleazar\"}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.notEquals("age", 4);
        assertEquals("\"$ne\" : {\"age\" : 4}", condition.toRecordString(Conditions.ROOT));
    }

    public void testNullCondition() {
        Condition condition = Conditions.isNull("name");
        assertEquals("\"name\": {\"$null\": \"1\"}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.isNotNull("name");
        assertEquals("\"name\": {\"$notNull\": \"1\"}", condition.toRecordString(Conditions.ROOT));
    }

    public void testOrCondition() {
        Condition condition = Conditions.or(Conditions.isNull("name"), Conditions.isNotNull("name"));
        assertEquals("\"$or\": {\"name\": {\"$null\": \"1\"},\"name\": {\"$notNull\": \"1\"}}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.or(Conditions.isNull("name"), Conditions.like("name", "%eleazar%"));
        assertEquals("\"$or\": {\"name\": {\"$null\": \"1\"},\"name\": { \"$like\" : \"%eleazar%\" }}", condition.toRecordString(Conditions.ROOT));
    }

    public void testAndCondition() {
        Condition condition = Conditions.and(Conditions.isNull("name"), Conditions.isNotNull("name"));
        assertEquals("\"name\": {\"$null\": \"1\"},\"name\": {\"$notNull\": \"1\"}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.and(Conditions.isNull("name"), Conditions.like("name", "%eleazar%"));
        assertEquals("\"name\": {\"$null\": \"1\"},\"name\": { \"$like\" : \"%eleazar%\" }", condition.toRecordString(Conditions.ROOT));
    }

    public void testGroupByClause() {
        GroupByClause clause = GroupByClause.groupBy(new String[]{"name", "age"});
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

    public void testJoinType() {
        assertEquals(JoinType.LEFT.toString(), "left");
        assertEquals(JoinType.RIGHT.toString(), "right");
        assertEquals(JoinType.INNER.toString(), "inner");
        assertEquals(JoinType.LEFT_OUTER.toString(), "left outer");
        assertEquals(JoinType.RIGHT_OUTER.toString(), "right outer");
    }

    public void testJoinClause() {
        RecordModel model = new RecordModel(234L);

        JoinClause join = Conditions.leftJoin(model, "user", "user.id = id");
        assertEquals("{ \"type\": \"left\", \"alias\": \"user\", \"target\": \"234\", \"on\": \"user.id = id\" }", join.toRecordString(Conditions.ROOT));

        join = Conditions.innerJoin(model, "user", "user.id like '%id%'");
        assertEquals("{ \"type\": \"inner\", \"alias\": \"user\", \"target\": \"234\", \"on\": \"user.id like '%id%'\" }", join.toRecordString(Conditions.ROOT));

        join = Conditions.innerJoin(model, "user", "user.id != id");
        assertEquals("{ \"type\": \"inner\", \"alias\": \"user\", \"target\": \"234\", \"on\": \"user.id != id\" }", join.toRecordString(Conditions.ROOT));

        join = Conditions.rightJoin(model, "user", "user.id != id");
        assertEquals("{ \"type\": \"right\", \"alias\": \"user\", \"target\": \"234\", \"on\": \"user.id != id\" }", join.toRecordString(Conditions.ROOT));
    }

    public void testBetweenCondition() {
        Condition condition = Conditions.between("age", 20, 40);

        assertEquals("\"age\": { \"$between\": [20,40]}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.between("date", "2015-11-01 00:00:00", "2015-11-01 23:59:59");

        assertEquals("\"date\": { \"$between\": [\"2015-11-01 00:00:00\",\"2015-11-01 23:59:59\"]}", condition.toRecordString(Conditions.ROOT));
    }

    public void testExistsCondition() {
        ExistsCondition condition = Conditions.exists(new RecordModel(1455545L), "inner");
        condition.add(Conditions.equals("inner.user_id", "vip.user_id"));

        assertEquals("\"$exists\": { \"joins\": [], \"model\": 1455545, \"alias\": \"inner\", \"where\": {\"inner.user_id\" : \"vip.user_id\"}}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.exists(new RecordModel(1455545L));
        condition.add(Conditions.equals("inner.user_id", "vip.user_id"));

        assertEquals("\"$exists\": { \"joins\": [], \"model\": 1455545, \"where\": {\"inner.user_id\" : \"vip.user_id\"}}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.exists();
        condition.add(Conditions.equals("inner.user_id", "vip.user_id"));

        assertEquals("\"$exists\": { \"joins\": [], \"where\": {\"inner.user_id\" : \"vip.user_id\"}}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.exists();
        condition.add(Conditions.equals("inner.user_id", "$vip.user_id$"));

        ExistsCondition innerCondition = Conditions.exists(new RecordModel(54545L), "inner2");
        innerCondition.add(Conditions.equals("inner2.user_id", "$vip2.user_id$"));

        condition.add(innerCondition);

        assertEquals("\"$exists\": { \"joins\": [], \"where\": {\"inner.user_id\" : \"$vip.user_id$\",\"$exists\": { \"joins\": [], \"model\": 54545, \"alias\": \"inner2\", \"where\": {\"inner2.user_id\" : \"$vip2.user_id$\"}}}}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.notExists(new RecordModel(1455545L), "inner");
        condition.add(Conditions.equals("inner.user_id", "vip.user_id"));

        assertEquals("\"$nexists\": { \"joins\": [], \"model\": 1455545, \"alias\": \"inner\", \"where\": {\"inner.user_id\" : \"vip.user_id\"}}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.notExists(new RecordModel(1455545L));
        condition.add(Conditions.equals("inner.user_id", "vip.user_id"));

        assertEquals("\"$nexists\": { \"joins\": [], \"model\": 1455545, \"where\": {\"inner.user_id\" : \"vip.user_id\"}}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.notExists();
        condition.add(Conditions.equals("inner.user_id", "vip.user_id"));

        assertEquals("\"$nexists\": { \"joins\": [], \"where\": {\"inner.user_id\" : \"vip.user_id\"}}", condition.toRecordString(Conditions.ROOT));

        condition = Conditions.notExists();
        condition.add(Conditions.equals("inner.user_id", "$vip.user_id$"));

        innerCondition = Conditions.notExists(new RecordModel(54545L), "inner2");
        innerCondition.add(Conditions.equals("inner2.user_id", "$vip2.user_id$"));

        condition.add(innerCondition);

        assertEquals("\"$nexists\": { \"joins\": [], \"where\": {\"inner.user_id\" : \"$vip.user_id$\",\"$nexists\": { \"joins\": [], \"model\": 54545, \"alias\": \"inner2\", \"where\": {\"inner2.user_id\" : \"$vip2.user_id$\"}}}}", condition.toRecordString(Conditions.ROOT));
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

        RecordModel model = new RecordModel(324L);

        List<JoinClause> joins = new LinkedList<>();

        assertEquals("\"joins\": []", DynamiCloudUtil.buildJoinTag(null));

        assertEquals("\"joins\": []", DynamiCloudUtil.buildJoinTag(joins));

        joins.add(Conditions.leftJoin(model, "user", "user.id = id"));

        assertEquals("\"joins\": [{ \"type\": \"left\", \"alias\": \"user\", \"target\": \"324\", \"on\": \"user.id = id\" }]", DynamiCloudUtil.buildJoinTag(joins));

        model = new RecordModel(325L);

        joins.add(Conditions.innerJoin(model, "countries", "user.id = id"));

        assertEquals("\"joins\": [{ \"type\": \"left\", \"alias\": \"user\", \"target\": \"324\", \"on\": \"user.id = id\" }, { \"type\": \"inner\", \"alias\": \"countries\", \"target\": \"325\", \"on\": \"user.id = id\" }]", DynamiCloudUtil.buildJoinTag(joins));

        joins = new LinkedList<>();

        joins.add(Conditions.leftOuterJoin(model, "user", "user.id = languages.id"));

        assertEquals("\"joins\": [{ \"type\": \"left outer\", \"alias\": \"user\", \"target\": \"325\", \"on\": \"user.id = languages.id\" }]", DynamiCloudUtil.buildJoinTag(joins));

        joins = new LinkedList<>();

        joins.add(Conditions.rightOuterJoin(model, "user", "user.id = languages.id"));

        assertEquals("\"joins\": [{ \"type\": \"right outer\", \"alias\": \"user\", \"target\": \"325\", \"on\": \"user.id = languages.id\" }]", DynamiCloudUtil.buildJoinTag(joins));
    }
}