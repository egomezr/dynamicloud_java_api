package org.dynamicloud.service;

import org.dynamicloud.exception.DynamiCloudServiceException;
import org.dynamicloud.net.http.HttpMethod;
import org.dynamicloud.net.http.RecordClient;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to call services
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/22/15
 **/
public class ServiceCallerImpl implements ServiceCaller {

    private static ServiceCaller instance;

    protected static ServiceCaller getInstance() {
        if (instance == null) {
            instance = new ServiceCallerImpl();
        }

        return instance;
    }

    /**
     * Will call a service.
     *
     * @param serviceUrl service url to be called
     * @throws DynamiCloudServiceException when any error occurred.
     */
    public ServiceResponse callService(String serviceUrl)
            throws DynamiCloudServiceException {
        return callService(serviceUrl, null);
    }

    /**
     * Will call a service using Post method.
     *
     * @param serviceUrl service url to be called
     * @param params     parameters
     * @throws DynamiCloudServiceException when any error occurred.
     */
    public ServiceResponse callService(String serviceUrl, Map<String, String> params)
            throws DynamiCloudServiceException {
        return callService(serviceUrl, params, HttpMethod.POST);
    }

    /**
     * Will call a service.
     *
     * @param serviceUrl service url to be called
     * @param params     parameters
     * @param method     method to use
     * @return a ServiceResponse from DynamiCloud servers
     * @throws DynamiCloudServiceException when any error occurred.
     */
    public ServiceResponse callService(String serviceUrl, Map<String, String> params, HttpMethod method)
            throws DynamiCloudServiceException {
        try {
            RecordClient client = RecordClient.Impl.getInstance();
            String response = client.executeRequest(new URI(serviceUrl),
                    params == null ? new HashMap<String, String>() : params, method);
            try {
                new JSONObject(response);
            } catch (Exception e) {
                throw new DynamiCloudServiceException("Invalid response from server.");
            }

            if (response == null) {
                throw new DynamiCloudServiceException("Response is null");
            } else {
                return new ServiceResponse(response);
            }
        } catch (Exception e) {
            throw new DynamiCloudServiceException(e.getMessage());
        }
    }

    /**
     * Will call a service.
     *
     * @param serviceUrl service url to be called
     * @param params     parameters
     * @param callback   callback to call success or error method
     * @throws DynamiCloudServiceException when any error occurred.
     */
    @Override
    public void callService(String serviceUrl, Map<String, String> params, ServiceCallback callback)
            throws DynamiCloudServiceException {
        try {
            RecordClient client = RecordClient.Impl.getInstance();

            String response = client.executeRequest(new URI(serviceUrl), params);
            try {
                new JSONObject(response);

            } catch (Exception e) {
                if (callback == null) {
                    throw new DynamiCloudServiceException(e.getMessage());
                }

                ServiceError error = new ServiceError("Response is not a JSON.", "");
                callback.error(error);

                return;
            }

            if (response == null) {
                if (callback == null) {
                    throw new DynamiCloudServiceException("Response is null");
                }

                ServiceError error = new ServiceError("Response is null", "---");
                callback.error(error);
            } else {
                if (callback != null) {
                    callback.success(new ServiceResponse(response));
                }
            }
        } catch (Exception e) {
            if (callback == null) {
                throw new DynamiCloudServiceException(e.getMessage());
            }

            ServiceError error = new ServiceError(e.getMessage(), "---");
            callback.error(error);
        }
    }
}