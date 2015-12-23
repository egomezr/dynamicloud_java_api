package org.dynamicloud.api;

import org.dynamicloud.api.model.RecordField;
import org.dynamicloud.api.model.RecordModel;
import org.dynamicloud.exception.DynamicloudProviderException;

import java.io.File;
import java.util.List;

/**
 * This interface declare all of method that a DynamicProvider implementation will have to implement.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/24/15
 **/
public interface DynamicProvider<T> {
    /**
     * Sets the credentials to this session
     *
     * @param credential the credentials to this session
     */
    void setRecordCredential(RecordCredential credential);

    /**
     * This method will load a record using rid and will instantiate an object with attributes bound to Model's fields.
     *
     * @param rid        record id
     * @param boundClass bound class to model's fields.
     * @return BoundInstance object related to boundClass
     */
    T loadRecord(Long rid, RecordModel model, Class boundClass);

    /**
     * This method will call an update operation in DynamiCloud servers
     * using model and BoundInstance object
     *
     * @param model    record model
     * @param instance bound instance object
     * @throws DynamicloudProviderException if something wrong occurred
     */
    void updateRecord(RecordModel model, T instance) throws DynamicloudProviderException;

    /**
     * This method will call a save operation in DynamiCloud servers
     * using model and BoundInstance object
     *
     * @param model    record model
     * @param instance bound instance object
     * @throws DynamicloudProviderException if something wrong occurred
     */
    void saveRecord(RecordModel model, T instance) throws DynamicloudProviderException;

    /**
     * This method will call a delete operation in DynamiCloud servers
     * using model and Record id
     *
     * @param model record model
     * @param rid   record id
     * @throws DynamicloudProviderException if something wrong occurred
     */
    void deleteRecord(RecordModel model, Long rid) throws DynamicloudProviderException;

    /**
     * Will create a RecordQuery and sets to this session
     *
     * @param recordModel model to use to execute operations
     * @return this RecordQuery instance
     */
    Query<T> createQuery(RecordModel recordModel);

    /**
     * Gets model record information from DynamiCloud servers.
     *
     * @param modelId model id in DynamiClod servers
     * @return RecordModel object
     * @throws DynamicloudProviderException if any error occurs
     */
    RecordModel loadModel(Long modelId) throws DynamicloudProviderException;

    /**
     * Loads all models related to CSK and ACI keys in DynamiCloud servers
     *
     * @return list of models
     * @throws DynamicloudProviderException if any error occurs
     */
    List<RecordModel> loadModels() throws DynamicloudProviderException;

    /**
     * Loads all model's fields according ModelID
     *
     * @param mid modelID
     * @return list of model's fields.
     * @throws DynamicloudProviderException
     */
    List<RecordField> loadFields(Long mid) throws DynamicloudProviderException;

    /**
     * Set the bound instance to get the fields and values that will used to update records
     *
     * @param boundInstance bondInstance with values
     * @return this instance of DynamicProvider
     * @throws DynamicloudProviderException if any error occurs
     */
    DynamicProvider setBoundInstance(T boundInstance) throws DynamicloudProviderException;

    /**
     * Executes an update using query as a selection and boundInstance with values
     *
     * @param query selection
     * @throws DynamicloudProviderException if any error occurs
     */
    void update(Query query) throws DynamicloudProviderException;

    /**
     * Executes a delete using query as a selection
     *
     * @param query selection
     * @throws DynamicloudProviderException if any error occurs
     */
    void delete(Query query) throws DynamicloudProviderException;
}