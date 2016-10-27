package org.dynamicloud.api;

import org.dynamicloud.api.model.RecordField;
import org.dynamicloud.api.model.RecordFieldType;
import org.dynamicloud.api.model.RecordModel;
import org.dynamicloud.exception.DynamicloudProviderException;
import org.dynamicloud.logger.LoggerTool;
import org.dynamicloud.net.http.HttpMethod;
import org.dynamicloud.service.ServiceCaller;
import org.dynamicloud.service.ServiceResponse;
import org.dynamicloud.util.ConfigurationProperties;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.*;

/**
 * This class implements DynamicProvider to execute CRUD operations.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/24/15
 **/
public class DynamicProviderImpl<T> implements DynamicProvider<T> {
    private static final LoggerTool log = LoggerTool.getLogger(DynamicProvider.class);
    private T boundInstance;
    private RecordCredential credentials;

    /**
     * Builds a provider
     */
    public DynamicProviderImpl(RecordCredential credential) {
        this.credentials = credential;
    }

    /**
     * Sets the credentials to this provider
     *
     * @param credential the credentials to this provider
     */
    @Override
    public void setRecordCredential(RecordCredential credential) {
        this.credentials = credential;
    }

    /**
     * This method will load a record using rid and will instantiate an object with attributes bound to Model's fields.
     *
     * @param rid        record id
     * @param boundClass bound class to model's fields.
     * @return BoundInstance object related to boundClass
     */
    @Override
    @SuppressWarnings("unchecked")
    public T loadRecord(Long rid, RecordModel model, Class boundClass) {
        String url = ConfigurationProperties.getInstance().getProperty("url");
        String urlGetRecords = ConfigurationProperties.getInstance().getProperty("url.get.record.info");

        try {
            urlGetRecords = url + urlGetRecords.replaceAll("\\{csk}", URLEncoder.encode(credentials.getCsk(), "UTF-8")).
                    replaceAll("\\{aci}", URLEncoder.encode(credentials.getAci(), "UTF-8"));
            urlGetRecords = urlGetRecords.replaceAll("\\{mid}", model.getId().toString());
            urlGetRecords = urlGetRecords.replaceAll("\\{rid}", rid.toString());

            ServiceResponse serviceResponse = ServiceCaller.Impl.getInstance().callService(urlGetRecords, null, HttpMethod.GET);

            JSONObject json = new JSONObject(serviceResponse.getResponse());
            T r = (T) boundClass.newInstance();

            DynamiCloudUtil.setData2record(r, json.getJSONObject("record"));

            return r;
        } catch (Exception e) {
            log.warn(e.getMessage());
        }

        return null;
    }

    /**
     * This method will call an update operation in Dynamicloud servers
     * using model and BoundInstance object
     *
     * @param model    record model
     * @param instance bound instance object
     * @throws DynamicloudProviderException if something wrong occurred
     */
    @Override
    public void updateRecord(RecordModel model, T instance) throws DynamicloudProviderException {
        if (!(instance instanceof BoundInstance)) {
            throw new IllegalArgumentException("instance param must be a BoundInstance");
        }

        String url = ConfigurationProperties.getInstance().getProperty("url");
        String urlGetRecords = ConfigurationProperties.getInstance().getProperty("url.update.record");

        try {
            urlGetRecords = url + urlGetRecords.replaceAll("\\{csk}", URLEncoder.encode(credentials.getCsk(), "UTF-8")).
                    replaceAll("\\{aci}", URLEncoder.encode(credentials.getAci(), "UTF-8"));
            urlGetRecords = urlGetRecords.replaceAll("\\{mid}", model.getId().toString());
            urlGetRecords = urlGetRecords.replaceAll("\\{rid}", ((BoundInstance) instance).getRecordId().toString());

            String fields = DynamiCloudUtil.buildFieldsJSON((BoundInstance) instance);

            Map<String, String> params = new HashMap<String, String>();
            params.put("fields", fields);

            ServiceResponse serviceResponse = ServiceCaller.Impl.getInstance().callService(urlGetRecords, params);

            JSONObject json = new JSONObject(serviceResponse.getResponse());
            if (json.getLong("status") != 200) {
                throw new RuntimeException(json.getString("message"));
            }
        } catch (Exception e) {
            throw new DynamicloudProviderException(e.getMessage());
        }
    }

    /**
     * This method will call a save operation in Dynamicloud servers
     * using model and BoundInstance object
     *
     * @param model    record model
     * @param instance bound instance object
     * @throws DynamicloudProviderException if something wrong occurred
     */
    public void saveRecord(RecordModel model, T instance) throws DynamicloudProviderException {
        if (!(instance instanceof BoundInstance)) {
            throw new IllegalArgumentException("instance param must be a BoundInstance");
        }

        String url = ConfigurationProperties.getInstance().getProperty("url");
        String urlGetRecords = ConfigurationProperties.getInstance().getProperty("url.save.record");

        try {
            urlGetRecords = url + urlGetRecords.replaceAll("\\{csk}", URLEncoder.encode(credentials.getCsk(), "UTF-8")).
                    replaceAll("\\{aci}", URLEncoder.encode(credentials.getAci(), "UTF-8"));
            urlGetRecords = urlGetRecords.replaceAll("\\{mid}", model.getId().toString());

            String fields = DynamiCloudUtil.buildFieldsJSON((BoundInstance) instance);

            Map<String, String> params = new HashMap<String, String>();
            params.put("fields", fields);

            ServiceResponse serviceResponse = ServiceCaller.Impl.getInstance().callService(urlGetRecords, params);

            JSONObject json = new JSONObject(serviceResponse.getResponse());
            if (json.getLong("status") != 200) {
                throw new RuntimeException(json.getString("message"));
            }

            long rid = json.getLong("rid");
            ((BoundInstance) instance).setRecordId(rid);
        } catch (Exception e) {
            throw new DynamicloudProviderException(e.getMessage());
        }
    }

    /**
     * This method will call a delete operation in DynamiCloud servers
     * using model and Record id
     *
     * @param model record model
     * @param rid   record id
     * @throws DynamicloudProviderException if something wrong occurred
     */
    public void deleteRecord(RecordModel model, Long rid) throws DynamicloudProviderException {
        String url = ConfigurationProperties.getInstance().getProperty("url");
        String urlGetRecords = ConfigurationProperties.getInstance().getProperty("url.delete.record");

        try {
            urlGetRecords = url + urlGetRecords.replaceAll("\\{csk}", URLEncoder.encode(credentials.getCsk(), "UTF-8")).
                    replaceAll("\\{aci}", URLEncoder.encode(credentials.getAci(), "UTF-8"));
            urlGetRecords = urlGetRecords.replaceAll("\\{mid}", model.getId().toString());
            urlGetRecords = urlGetRecords.replaceAll("\\{rid}", rid.toString());

            ServiceResponse serviceResponse = ServiceCaller.Impl.getInstance().callService(urlGetRecords, null,
                    HttpMethod.DELETE);

            JSONObject json = new JSONObject(serviceResponse.getResponse());
            if (json.getLong("status") != 200) {
                throw new RuntimeException(json.getString("message"));
            }
        } catch (Exception e) {
            throw new DynamicloudProviderException(e.getMessage());
        }
    }

    /**
     * Will create a RecordQuery and sets to this provider
     *
     * @param recordModel model to use to execute operations
     * @return this RecordQuery instance
     */
    @Override
    public Query<T> createQuery(RecordModel recordModel) {
        RecordQuery<T> recordQuery = new RecordQuery<T>(recordModel);
        recordQuery.setCredentials(credentials);

        return recordQuery;
    }

    /**
     * Gets model record information from Dynamicloud servers.
     *
     * @param modelId model id in DynamiClod servers
     * @return RecordModel object
     * @throws DynamicloudProviderException if any error occurs
     */
    @Override
    public RecordModel loadModel(Long modelId) throws DynamicloudProviderException {
        String url = ConfigurationProperties.getInstance().getProperty("url");
        String urlGetRecords = ConfigurationProperties.getInstance().getProperty("url.get.model.info");

        try {
            urlGetRecords = url + urlGetRecords.replaceAll("\\{csk}", URLEncoder.encode(credentials.getCsk(), "UTF-8")).
                    replaceAll("\\{aci}", URLEncoder.encode(credentials.getAci(), "UTF-8"));
            urlGetRecords = urlGetRecords.replaceAll("\\{mid}", modelId.toString());

            ServiceResponse serviceResponse = ServiceCaller.Impl.getInstance().callService(urlGetRecords, null,
                    HttpMethod.GET);

            JSONObject json = new JSONObject(serviceResponse.getResponse());

            if (json.getLong("status") != 200) {
                throw new RuntimeException(json.getString("message"));
            }

            RecordModel model = new RecordModel(modelId, null);
            model.setName(json.getString("name"));
            model.setDescription(json.getString("description"));

            return model;
        } catch (Exception e) {
            throw new DynamicloudProviderException(e.getMessage());
        }
    }

    /**
     * Loads all models related to CSK and ACI keys in Dynamicloud servers
     *
     * @return list of models
     * @throws DynamicloudProviderException if any error occurs
     */
    @Override
    public List<RecordModel> loadModels() throws DynamicloudProviderException {
        String url = ConfigurationProperties.getInstance().getProperty("url");
        String urlGetRecords = ConfigurationProperties.getInstance().getProperty("url.get.models");

        try {
            urlGetRecords = url + urlGetRecords.replaceAll("\\{csk}", URLEncoder.encode(credentials.getCsk(), "UTF-8")).
                    replaceAll("\\{aci}", URLEncoder.encode(credentials.getAci(), "UTF-8"));

            ServiceResponse serviceResponse = ServiceCaller.Impl.getInstance().callService(urlGetRecords, null,
                    HttpMethod.GET);

            JSONObject json = new JSONObject(serviceResponse.getResponse());

            if (json.getLong("status") != 200) {
                throw new RuntimeException(json.getString("message"));
            }

            List<RecordModel> models = new LinkedList<RecordModel>();
            JSONArray array = json.getJSONArray("models");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);

                RecordModel model = new RecordModel(jsonObject.getLong("id"), null);
                model.setName(jsonObject.getString("name"));
                model.setDescription(jsonObject.getString("description"));

                models.add(model);
            }

            return models;
        } catch (Exception e) {
            throw new DynamicloudProviderException(e.getMessage());
        }
    }

    /**
     * Loads all model's fields according ModelID
     *
     * @param mid modelID
     * @return list of model's fields.
     * @throws DynamicloudProviderException
     */
    @Override
    public List<RecordField> loadFields(Long mid) throws DynamicloudProviderException {
        String url = ConfigurationProperties.getInstance().getProperty("url");
        String urlGetRecords = ConfigurationProperties.getInstance().getProperty("url.get.fields");

        try {
            urlGetRecords = url + urlGetRecords.replaceAll("\\{csk}", URLEncoder.encode(credentials.getCsk(), "UTF-8")).
                    replaceAll("\\{aci}", URLEncoder.encode(credentials.getAci(), "UTF-8"));
            urlGetRecords = urlGetRecords.replaceAll("\\{mid}", mid.toString());

            ServiceResponse serviceResponse = ServiceCaller.Impl.getInstance().callService(urlGetRecords, null,
                    HttpMethod.GET);

            JSONObject json = new JSONObject(serviceResponse.getResponse());

            if (json.getLong("status") != 200) {
                throw new RuntimeException(json.getString("message"));
            }

            JSONObject fs = json.getJSONObject("fields");

            List<RecordField> fields = new LinkedList<RecordField>();
            Iterator keys = fs.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();

                JSONObject jf = fs.getJSONObject(key);

                RecordField field = new RecordField(mid);
                field.setId(jf.getLong("id"));
                field.setIdentifier(jf.getString("identifier"));
                field.setLabel(jf.getString("label"));
                field.setComment(jf.getString("comment"));
                field.setUniqueness(jf.getBoolean("uniqueness"));
                field.setUniqueness(jf.getBoolean("required"));
                field.setType(RecordFieldType.getFieldType(jf.getInt("field_type")));
                field.setItems(DynamiCloudUtil.buildItems(jf.getJSONArray("items")));
                field.setMid(mid);

                fields.add(field);
            }

            return fields;
        } catch (Exception e) {
            throw new DynamicloudProviderException(e.getMessage());
        }
    }

    /**
     * This method will make a request to generate a link to download the file related to this recordId and fieldName
     *
     * @param modelId   model id
     * @param recordId  record id
     * @param fieldName field name
     * @return link to share file
     */
    public String shareFile(Long modelId, Long recordId, String fieldName) throws DynamicloudProviderException {
        String url = ConfigurationProperties.getInstance().getProperty("url");
        String urlGetRecords = ConfigurationProperties.getInstance().getProperty("url.share.file");

        try {
            urlGetRecords = url + urlGetRecords.replaceAll("\\{csk}", URLEncoder.encode(credentials.getCsk(), "UTF-8")).
                    replaceAll("\\{aci}", URLEncoder.encode(credentials.getAci(), "UTF-8"));
            urlGetRecords = urlGetRecords.replaceAll("\\{mid}", modelId.toString()).
                    replaceAll("\\{rid}", recordId.toString()).replaceAll("\\{identifier}", fieldName);

            ServiceResponse serviceResponse = ServiceCaller.Impl.getInstance().callService(urlGetRecords, null, HttpMethod.GET);

            JSONObject json = new JSONObject(serviceResponse.getResponse());

            if (json.getLong("status") != 200) {
                throw new RuntimeException(json.getString("message"));
            }

            return json.getString("link");
        } catch (Exception e) {
            throw new DynamicloudProviderException(e.getMessage());
        }
    }

    /**
     * Set the bound instance to get the fields and values that will used to update records
     *
     * @param boundInstance bondInstance with values
     * @return this instance of DynamicProvider
     * @throws DynamicloudProviderException if any error occurs
     */
    @Override
    public DynamicProvider setBoundInstance(T boundInstance) throws DynamicloudProviderException {
        this.boundInstance = boundInstance;

        return this;
    }

    /**
     * Executes an update using query as a selection and boundInstance with values
     * Dynamicloud will normalize the key pair values.  That is, will be used field identifiers only.
     *
     * @param query selection
     * @throws DynamicloudProviderException if any error occurs
     */
    @Override
    @SuppressWarnings("unchecked")
    public void update(Query query) throws DynamicloudProviderException {
        if (boundInstance == null) {
            throw new IllegalStateException("BoundInstance is null and this object has the values to update records.");
        }

        if (!(boundInstance instanceof BoundInstance)) {
            throw new IllegalArgumentException("BoundInstance param must be a BoundInstance");
        }

        String selection = DynamiCloudUtil.buildString(query.getConditions(), null, null, null);
        String fields = "{\"updates\": " + DynamiCloudUtil.buildFieldsJSON((BoundInstance) boundInstance) + "}";

        String url = ConfigurationProperties.getInstance().getProperty("url");
        String urlGetRecords = ConfigurationProperties.getInstance().getProperty("url.update.selection");

        try {
            urlGetRecords = url + urlGetRecords.replaceAll("\\{csk}", URLEncoder.encode(credentials.getCsk(), "UTF-8")).
                    replaceAll("\\{aci}", URLEncoder.encode(credentials.getAci(), "UTF-8"));
            urlGetRecords = urlGetRecords.replaceAll("\\{mid}", query.getModel().getId().toString());

            Map<String, String> params = new HashMap<String, String>();
            params.put("fields", fields);
            params.put("selection", selection);

            ServiceResponse serviceResponse = ServiceCaller.Impl.getInstance().callService(urlGetRecords, params);

            JSONObject json = new JSONObject(serviceResponse.getResponse());

            if (json.getLong("status") != 200) {
                throw new RuntimeException(json.getString("message"));
            }
        } catch (Exception e) {
            throw new DynamicloudProviderException(e.getMessage());
        }

    }

    /**
     * Executes a delete using query as a selection
     *
     * @param query selection
     * @throws DynamicloudProviderException if any error occurs
     */
    @Override
    @SuppressWarnings("unchecked")
    public void delete(Query query) throws DynamicloudProviderException {
        String selection = DynamiCloudUtil.buildString(query.getConditions(), null, null, null);

        String url = ConfigurationProperties.getInstance().getProperty("url");
        String urlGetRecords = ConfigurationProperties.getInstance().getProperty("url.delete.selection");

        try {
            urlGetRecords = url + urlGetRecords.replaceAll("\\{csk}", URLEncoder.encode(credentials.getCsk(), "UTF-8")).
                    replaceAll("\\{aci}", URLEncoder.encode(credentials.getAci(), "UTF-8"));
            urlGetRecords = urlGetRecords.replaceAll("\\{mid}", query.getModel().getId().toString());

            Map<String, String> params = new HashMap<String, String>();
            params.put("selection", selection);

            ServiceResponse serviceResponse = ServiceCaller.Impl.getInstance().callService(urlGetRecords, params);

            JSONObject json = new JSONObject(serviceResponse.getResponse());

            if (json.getLong("status") != 200) {
                throw new RuntimeException(json.getString("message"));
            }
        } catch (Exception e) {
            throw new DynamicloudProviderException(e.getMessage());
        }
    }
}