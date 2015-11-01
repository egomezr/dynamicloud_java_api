package org.dynamicloud.service;

/**
 * This class is a callback to handle success and error situation
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/26/15
 **/
public interface ServiceCallback {
    /**
     * This method will be called when the response code is ok
     *
     * @param response response from Dynamicloud servers
     */
    void success(ServiceResponse response);

    /**
     * This method will be called when the response code is not ok
     *
     * @param error is a bean with a message and code returned from Dynamicloud servers.
     */
    void error(ServiceError error);
}
