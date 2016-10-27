package org.dynamicloud.net.http;

import org.dynamicloud.exception.DynamiCloudServiceException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * This class represents a http client like a browser
 * <p/>
 * to execute request on <a href="https://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/23/15
 **/
public interface RecordClient {

    /**
     * Execute a request using form parameters
     * Default method POST
     *
     * @param uri    uri target
     * @param params parameters that will be sent in request
     * @return response from <a href="https://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
     */
    String executeRequest(URI uri, Map<String, String> params) throws DynamiCloudServiceException;

    /**
     * Execute a request using form parameters
     *
     * @param uri    uri target
     * @param params parameters that will be sent in request
     * @param method method to use
     * @return response from <a href="https://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
     */
    String executeRequest(URI uri, Map<String, String> params, HttpMethod method) throws DynamiCloudServiceException;

    class Impl {
        public static RecordClient getInstance() {
            return RecordClientImpl.getInstance();
        }
    }
}