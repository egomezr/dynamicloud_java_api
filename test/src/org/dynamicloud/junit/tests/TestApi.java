package org.dynamicloud.junit.tests;

import junit.framework.TestCase;
import org.dynamicloud.api.*;
import org.dynamicloud.api.criteria.*;
import org.dynamicloud.api.model.RecordField;
import org.dynamicloud.api.model.RecordModel;
import org.dynamicloud.exception.DynamicloudProviderException;
import org.dynamicloud.junit.bean.DateBean;
import org.dynamicloud.junit.bean.JoinResultBean;
import org.dynamicloud.junit.bean.ModelFields;

import java.io.File;
import java.sql.Date;
import java.util.List;

/**
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 10/16/15
 **/
@SuppressWarnings("ResultOfMethodCallIgnored")
public class TestApi extends TestCase {
    public static final String CSK = "csk#...";
    public static final String ACI = "aci#...";
    private final static String FILE_PATH = "/file.sql";
    private final static String TEST_CASE_FILE = "/test_file.sql";
    private static long modelId = -1;
    private static long auxModelId = -1;
    private static long dateModelId = -1;

    private static RecordModel recordModel = new RecordModel(modelId);
    private static RecordModel auxRecordModel = new RecordModel(auxModelId);
    private static DynamicProvider<ModelFields> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));

    public void testEqualsNotEquals() {
        try {
            Query query = provider.createQuery(recordModel);
            RecordResults results = query.add(Conditions.equals("email", "ego@gmail.com")).list();
            assertEquals(1, results.getRecords().size());

            query = provider.createQuery(recordModel);
            results = query.add(Conditions.equals("agefield", 23)).list();
            assertEquals(1, results.getRecords().size());

            query = provider.createQuery(recordModel);
            results = query.add(Conditions.equals("email", "nxnxnxnxn")).list();
            assertEquals(0, results.getRecords().size());

            query = provider.createQuery(recordModel);
            results = query.add(Conditions.notEquals("email", "nxnxnxnxn")).list();
            assertEquals(2, results.getRecords().size());
            assertEquals(2, results.getFastReturnedSize());
            assertEquals(2, results.getTotalRecords());

        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testGreaterThanEquals() {
        Query query = provider.createQuery(recordModel);
        try {
            RecordResults results = query.add(Conditions.greaterEquals("agefield", 23)).list();
            assertEquals(2, results.getRecords().size());

            results = query.add(Conditions.greaterEquals("agefield", 24)).list();
            assertEquals(1, results.getRecords().size());

            results = query.add(Conditions.greaterEquals("agefield", 40)).list();
            assertEquals(1, results.getRecords().size());

            results = query.add(Conditions.greaterThan("agefield", 40)).list();
            assertEquals(0, results.getRecords().size());

            results = query.add(Conditions.greaterThan("agefield", 23)).list();
            assertEquals(1, results.getRecords().size());

            results = query.add(Conditions.greaterThan("agefield", 22)).list();
            assertEquals(2, results.getRecords().size());

            results = query.add(Conditions.lesserThan("agefield", 22)).list();
            assertEquals(0, results.getRecords().size());

            results = query.add(Conditions.lesserThan("agefield", 40)).list();
            assertEquals(1, results.getRecords().size());

            results = query.add(Conditions.lesserThan("agefield", 23)).list();
            assertEquals(0, results.getRecords().size());

            results = query.add(Conditions.lesserThan("agefield", 24)).list();
            assertEquals(1, results.getRecords().size());
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testLike() {
        Query query = provider.createQuery(recordModel);
        try {
            RecordResults results = query.add(Conditions.like("email", "ego%")).list();
            assertEquals(1, results.getRecords().size());

            results = query.add(Conditions.like("email", "%.com")).list();
            assertEquals(2, results.getRecords().size());

            results = query.add(Conditions.like("email", "%e%")).list();
            assertEquals(2, results.getRecords().size());

            results = query.add(Conditions.notLike("email", "%ego%")).list();
            assertEquals(1, results.getRecords().size());

            results = query.add(Conditions.notLike("email", "%.com")).list();
            assertEquals(0, results.getRecords().size());

            results = query.add(Conditions.notLike("email", "%eleazar%")).list();
            assertEquals(2, results.getRecords().size());
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testIn() {
        Query query = provider.createQuery(recordModel);
        try {
            RecordResults results = query.add(Conditions.in("email", new String[]{"ego@gmail.com"})).list();
            assertEquals(1, results.getRecords().size());

            results = query.add(Conditions.in("email", new String[]{"eego@gmail.com"})).list();
            assertEquals(0, results.getRecords().size());

            results = query.add(Conditions.notIn("email", new String[]{"eego@gmail.com"})).list();
            assertEquals(2, results.getRecords().size());

            results = query.add(Conditions.notIn("email", new String[]{"ego@gmail.com"})).list();
            assertEquals(1, results.getRecords().size());
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testOffSet() {
        Query query = provider.createQuery(recordModel);
        try {
            query.add(Conditions.like("email", "%.com%")).setOffset(1).setCount(1);
            RecordResults results = query.list();
            assertEquals(1, results.getRecords().size());

            query.add(Conditions.like("email", "%.com%")).setOffset(0).setCount(2);
            results = query.list();
            assertEquals(2, results.getRecords().size());

            query.add(Conditions.like("email", "%.com%")).setOffset(2).setCount(2);
            results = query.list();
            assertEquals(0, results.getRecords().size());
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void testProjection() {
        Query query = provider.createQuery(recordModel);
        try {
            recordModel.setBoundClass(ModelFields.class);

            query.add(Conditions.like("email", "%.com%"));
            /**
             * We have to associate an alias for this projection and it should match with a set method in ModelFields.class
             */
            query.setProjection("min(email) email");

            RecordResults<ModelFields> results = query.list();
            assertEquals("ego@gmail.com", results.getRecords().get(0).getEmail());

            /**
             * We have to associate an alias for this projection and it should match with a set method in ModelFields.class
             */
            query.setProjection("max(email) email");

            results = query.list();
            assertEquals("elea@yahoo.com", results.getRecords().get(0).getEmail());

            /**
             * We have to associate an alias for this projection and it should match with a set method in ModelFields.class
             */
            query.setProjection("avg(agefield) number");

            results = query.list();
            assertEquals(31.5, results.getRecords().get(0).getNumberResult());

            /**
             * We have to associate an alias for this projection and it should match with a set method in ModelFields.class
             */
            query.setProjection("sum(agefield) number");

            results = query.list();
            assertEquals(63.0, results.getRecords().get(0).getNumberResult());

            /**
             * We have to associate an alias for this projection and it should match with a set method in ModelFields.class
             */
            query.setProjection("distinct(email)");

            results = query.list();
            assertEquals(2, results.getRecords().size());

            /**
             * We have to associate an alias for this projection and it should match with a set method in ModelFields.class
             */
            query.setProjection("count(email) number");

            results = query.list();
            assertEquals(2, results.getRecords().get(0).getNumberResult());

            query.setProjection("email");

            results = query.setCount(1).setOffset(1).list();
            assertEquals("elea@yahoo.com", results.getRecords().get(0).getEmail());

            query.setProjection("agefield as result");

            results = query.setCount(1).setOffset(1).list();
            assertEquals(40, Integer.parseInt(results.getRecords().get(0).getStringResult()));

        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void testUpdateSelection() {
        try {
            ModelFields instance = new ModelFields();
            instance.setBirthdat("2015-05-23");
            instance.setEmail("ego@gmail.com");

            provider.setBoundInstance(instance);

            Query query = provider.createQuery(recordModel).add(Conditions.equals("email", "ego@gmail.com"));
            recordModel.setBoundClass(ModelFields.class);
            provider.update(query);

            RecordResults<ModelFields> results = query.add(Conditions.equals("email", "ego@gmail.com")).list();
            assertEquals("2015-05-23", results.getRecords().get(0).getBirthdat());
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testLoadRecord() {
        ModelFields instance = provider.loadRecord(2L, recordModel, ModelFields.class);
        assertEquals("ego@gmail.com", instance.getEmail());
    }

    public void testBigTextField() {
        ModelFields instance = provider.loadRecord(2L, recordModel, ModelFields.class);
        assertEquals(null, instance.getPhoto());
    }

    public void testUpdateRecord() {

        try {
            ModelFields instance = provider.loadRecord(2L, recordModel, ModelFields.class);
            instance.setBirthdat("2015-05-24");

            provider.updateRecord(recordModel, instance);

            instance = provider.loadRecord(2L, recordModel, ModelFields.class);

            assertEquals("2015-05-24", instance.getBirthdat());
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testCreateDeleteRecord() {
        try {
            ModelFields instance = provider.loadRecord(2L, recordModel, ModelFields.class);
            instance.setRecordId(null);
            instance.setEmail("eeee@gmail.com");

            provider.saveRecord(recordModel, instance);

            long rid = instance.getRecordId().longValue();

            provider.deleteRecord(recordModel, rid);

            instance = null;
            try {
                instance = provider.loadRecord(rid, recordModel, ModelFields.class);
            } catch (Exception ignore) {
            }

            assertEquals(null, instance);

            DateBean bean = new DateBean();
            bean.setDate(new Date(System.currentTimeMillis()));

            DynamicProvider<DateBean> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));
            provider.saveRecord(new RecordModel(dateModelId), bean);

            assertNotNull(bean.getRecordId());

            DateBean dateBean = provider.loadRecord(bean.getRecordId().longValue(), new RecordModel(dateModelId), DateBean.class);

            assertNotNull(dateBean.getDate());

            provider.deleteRecord(new RecordModel(dateModelId), bean.getRecordId().longValue());
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testDeleteSelection() {
        try {
            ModelFields instance = provider.loadRecord(2L, recordModel, ModelFields.class);
            instance.setRecordId(null);
            instance.setEmail("eeee@gmail.com");

            provider.saveRecord(recordModel, instance);

            long rid = instance.getRecordId().longValue();

            Query query = provider.createQuery(recordModel).add(Conditions.equals("email", "eeee@gmail.com"));
            provider.delete(query);

            instance = null;
            try {
                instance = provider.loadRecord(rid, recordModel, ModelFields.class);
            } catch (Exception ignore) {
            }

            assertEquals(null, instance);
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testLoadModel() {
        try {
            RecordModel model = provider.loadModel(recordModel.getId());
            assertEquals("Model#41", model.getName());
            assertEquals(recordModel.getId(), model.getId());
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testLoadModels() {
        try {
            List<RecordModel> models = provider.loadModels();
            assertTrue(models.size() >= 2);
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testLoadFields() {
        try {
            List<RecordField> fields = provider.loadFields(recordModel.getId());
            assertNotNull(fields);
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void testNextMethod() {
        try {
            Query<ModelFields> query = provider.createQuery(recordModel).add(Conditions.like("email", "%.com%")).
                    setCount(1).setOffset(0);

            RecordResults<ModelFields> results = query.list();

            assertEquals(23, results.getRecords().get(0).getAgeField());

            results = query.next();

            assertEquals(40, results.getRecords().get(0).getAgeField());

            results = query.next();

            assertEquals(0, results.getFastReturnedSize());
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testItemValues() {
        ModelFields instance = provider.loadRecord(2l, recordModel, ModelFields.class);
        instance.setCountry("bra");

        instance = provider.loadRecord(2l, recordModel, ModelFields.class);

        assertEquals("bra", instance.getCountry());
    }

    public void testMultiItemValues() {
        try {
            ModelFields instance = provider.loadRecord(2l, recordModel, ModelFields.class);
            instance.setPassword(new String[]{"01", "02"});

            provider.updateRecord(recordModel, instance);

            instance = provider.loadRecord(2l, recordModel, ModelFields.class);

            assertEquals("01", instance.getPassword()[0]);
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testShareDownUploadFile() {
        try {
            provider.uploadFile(recordModel.getId(), 2l, "photo", new File(FILE_PATH),
                    "application/text", "ThisIsAnExample.sql");

            String link = provider.shareFile(recordModel.getId(), 2l, "photo");

            assertNotNull(link);

            File file = new File(TEST_CASE_FILE);
            if (file.exists()) {
                file.delete();
            }

            provider.downloadFile(recordModel.getId(), 2l, "photo", file);

            assertTrue(new File(TEST_CASE_FILE).exists());
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testAliasPresenceJoin() {
        DynamicProvider<JoinResultBean> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));
        Query<JoinResultBean> query = provider.createQuery(recordModel);

        try {
            /**
             * It is important the receptor bean to attach the results to this bean.
             */
            query.join(Conditions.innerJoin(auxRecordModel, "aux", "user.id = aux.modelid"));

            query.list();

            fail("Server didn't validate alias presence.");
        } catch (DynamicloudProviderException ignore) {

        }
    }

    public void testInnerJoin() {
        DynamicProvider<JoinResultBean> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));
        Query<JoinResultBean> query = provider.createQuery(recordModel);

        try {
            /**
             * This is the alias to recordModel, this alias is necessary to use JoinClause
             */
            query.setAlias("user");

            /**
             * It is important the receptor bean to attach the results to this bean.
             */
            recordModel.setBoundClass(JoinResultBean.class);

            query.setProjection(new String[]{"user.country as country", "aux.birthdat as birthdate"});
            query.join(Conditions.innerJoin(auxRecordModel, "aux", "user.id = aux.modelid"));
            query.orderBy("user.country").asc();

            RecordResults<JoinResultBean> results = query.list();

            if (results.getFastReturnedSize() > 0) {
                JoinResultBean bean = results.getRecords().get(0);

                assertEquals("bra", bean.getCountry());
                assertEquals("2015-11-11", results.getRecords().get(0).getBirthDate());
            } else {
                fail("Without results.  That's wrong!");
            }

        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testLeftJoin() {
        DynamicProvider<JoinResultBean> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));
        Query<JoinResultBean> query = provider.createQuery(recordModel);

        try {
            /**
             * This is the alias to recordModel, this alias is necessary to use JoinClause
             */
            query.setAlias("user");

            /**
             * It is important the receptor bean to attach the results to this bean.
             */
            recordModel.setBoundClass(JoinResultBean.class);

            query.setProjection(new String[]{"user.country as country", "aux.birthdat as birthdate"});
            query.join(Conditions.leftJoin(auxRecordModel, "aux", "user.id = aux.modelid"));
            query.orderBy("user.country").asc();

            RecordResults<JoinResultBean> results = query.list();

            if (results.getFastReturnedSize() > 0) {
                JoinResultBean bean = results.getRecords().get(0);

                assertEquals("bra", bean.getCountry());
                assertEquals("2015-11-11", results.getRecords().get(0).getBirthDate());
            } else {
                fail("Without results.  That's wrong!");
            }

        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testRightJoin() {
        DynamicProvider<JoinResultBean> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));
        Query<JoinResultBean> query = provider.createQuery(recordModel);

        try {
            /**
             * This is the alias to recordModel, this alias is necessary to use JoinClause
             */
            query.setAlias("user");

            /**
             * It is important the receptor bean to attach the results to this bean.
             */
            recordModel.setBoundClass(JoinResultBean.class);

            query.setProjection(new String[]{"user.country as country", "aux.birthdat as birthdate"});
            query.join(Conditions.leftJoin(auxRecordModel, "aux", "user.id = aux.modelid"));
            query.orderBy("user.country").asc();

            RecordResults<JoinResultBean> results = query.list();

            if (results.getFastReturnedSize() > 0) {
                JoinResultBean bean = results.getRecords().get(0);

                assertEquals("bra", bean.getCountry());
                assertEquals("2015-11-11", results.getRecords().get(0).getBirthDate());
            } else {
                fail("Without results.  That's wrong!");
            }

        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testJoinAndSelection() {
        DynamicProvider<JoinResultBean> provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));
        Query<JoinResultBean> query = provider.createQuery(recordModel);

        try {
            /**
             * This is the alias to recordModel, this alias is necessary to use JoinClause
             */
            query.setAlias("user");

            /**
             * It is important the receptor bean to attach the results to this bean.
             */
            recordModel.setBoundClass(JoinResultBean.class);

            query.setProjection(new String[]{"user.country as country", "aux.birthdat as birthdate"});
            query.join(Conditions.leftJoin(auxRecordModel, "aux", "user.id = aux.modelid"));
            query.add(Conditions.notEquals("aux.birthdat", "2015-09-15"));

            query.orderBy("user.country").asc();

            RecordResults<JoinResultBean> results = query.list();

            if (results.getFastReturnedSize() > 0) {
                JoinResultBean bean = results.getRecords().get(0);

                assertEquals("bra", bean.getCountry());
                assertEquals("2015-11-11", bean.getBirthDate());
            } else {
                fail("Without results.  That's wrong!");
            }

        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testExists() {
        DynamicProvider provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));
        Query query = provider.createQuery(new RecordModel(modelId));

        /**
         * This is the alias to recordModel, this alias is necessary if you need to execute an exists using two models
         */
        query.setAlias("user");

        ExistsCondition exists = Conditions.exists(auxRecordModel, "aux");

        /**
         * The dollar symbols are to avoid to use right part as a String
         */
        exists.add(Conditions.notEquals("user.id", "$aux.modelid$"));

        query.add(exists);

        try {
            RecordResults results = query.list();
            assertTrue(results.getFastReturnedSize() == 2);
            assertFalse(results.getRecords().isEmpty());
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testExistsJoin() {
        DynamicProvider provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));
        Query query = provider.createQuery(new RecordModel(modelId));

        /**
         * This is the alias to recordModel, this alias is necessary if you need to execute an exists using two models
         */
        query.setAlias("user");

        ExistsCondition exists = Conditions.exists(auxRecordModel, "aux");
        exists.join(Conditions.innerJoin(auxRecordModel, "auxx", "aux.id = auxx.modelid"));

        /**
         * The dollar symbols are to avoid to use right part as a String
         */
        exists.add(Conditions.notEquals("user.id", "$aux.modelid$"));

        query.add(exists);

        try {
            RecordResults results = query.list();
            assertTrue(results.getFastReturnedSize() == 2);
            assertFalse(results.getRecords().isEmpty());
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testNotExists() {
        DynamicProvider provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));
        Query query = provider.createQuery(new RecordModel(modelId));

        /**
         * This is the alias to recordModel, this alias is necessary if you need to execute an exists using two models
         */
        query.setAlias("user");

        ExistsCondition exists = Conditions.notExists(auxRecordModel, "aux");

        /**
         * The dollar symbol is to avoid to use right part as a String
         */
        exists.add(Conditions.equals("user.id", "$aux.modelid$"));

        query.add(exists);

        try {
            RecordResults results = query.list();

            assertTrue(results.getFastReturnedSize() == 0);
            assertTrue(results.getRecords().isEmpty());
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }

    public void testBetween() {
        DynamicProvider provider = new DynamicProviderImpl<>(new RecordCredential(CSK, ACI));
        try {
            Query query = provider.createQuery(new RecordModel(modelId));
            query.add(Conditions.between("agefield", 20, 25));

            RecordResults results = query.list();

            assertTrue(results.getFastReturnedSize() == 1);
            Record record = (RecordImpl) results.getRecords().get(0);

            assertNotNull(record);

            query = provider.createQuery(new RecordModel(dateModelId));
            query.add(Conditions.between("datefie", "2015-11-28 00:00:00", "2015-11-28 23:59:59"));

            results = query.list();

            assertTrue(results.getFastReturnedSize() > 1);
            record = (RecordImpl) results.getRecords().get(0);

            assertNotNull(record);

            query = provider.createQuery(new RecordModel(dateModelId));
            query.add(Conditions.between("datefie", "2015-11-28 01:00:00", "2015-11-28 23:59:59"));

            results = query.list();
            assertTrue(results.getFastReturnedSize() == 0);
        } catch (DynamicloudProviderException e) {
            fail(e.getMessage());
        }
    }
}