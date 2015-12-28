package org.dynamicloud.service;

import org.dynamicloud.exception.DynamiCloudServiceException;
import org.dynamicloud.net.http.HttpMethod;

import java.io.File;
import java.util.Map;

/**
 * This interface will contain all those methods to call web services in Dynamicloud servers.
 * Basically, this interface will be implement by a class that offers wrapped http method.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 2015-08-22
 **/
public interface ServiceCaller {

    /**
     * Will call a service.
     *
     * @param serviceUrl service url to be called
     * @throws DynamiCloudServiceException when any error occurred.
     */
    ServiceResponse callService(String serviceUrl)
            throws DynamiCloudServiceException;

    /**
     * Will call a service using Post method.
     *
     * @param serviceUrl service url to be called
     * @param params     parameters
     * @throws DynamiCloudServiceException when any error occurred.
     */
    ServiceResponse callService(String serviceUrl, Map<String, String> params)
            throws DynamiCloudServiceException;

    /**
     * Will call a service.
     *
     * @param serviceUrl service url to be called
     * @param params     parameters
     * @param method     method to use
     * @throws DynamiCloudServiceException when any error occurred.
     */
    ServiceResponse callService(String serviceUrl, Map<String, String> params, HttpMethod method)
            throws DynamiCloudServiceException;

    /**
     * Will call a service.
     *
     * @param serviceUrl service url to be called
     * @param params     parameters
     * @param callback   callback to call success or error method
     * @throws DynamiCloudServiceException when any error occurred.
     */
    void callService(String serviceUrl, Map<String, String> params, ServiceCallback callback) throws DynamiCloudServiceException;

    class Impl {
        public static ServiceCaller getInstance() {
            return ServiceCallerImpl.getInstance();
        }
    }
}