package org.dynamicloud.junit.tests;

import junit.framework.TestCase;
import org.dynamicloud.api.*;
import org.dynamicloud.api.criteria.Conditions;
import org.dynamicloud.api.model.RecordField;
import org.dynamicloud.api.model.RecordModel;
import org.dynamicloud.exception.DynamicloudProviderException;
import org.dynamicloud.junit.bean.ModelFields;

import java.io.File;
import java.util.List;

/**
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 10/16/15
 **/
@SuppressWarnings("ResultOfMethodCallIgnored")
public class TestApi extends TestCase {
    public static final String CSK = "csk#f66d45541a562558c1854b79cf9cc0c7a4c5c209";
    public static final String ACI = "aci#12ea40b29cbbc6a90289838d7e800be3fe8f9868";

    private static long modelId = 980190974;
    private static RecordModel recordModel = new RecordModel(modelId);

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
            assertEquals(2, models.size());
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

    private final static String FILE_PATH = "/Users/egomezr/Documents/upload.sql";
    private final static String TEST_CASE_FILE = "/Users/egomezr/Documents/TEST_CASE_FILE.sql";

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
}