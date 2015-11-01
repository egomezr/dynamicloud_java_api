package org.dynamicloud.net.http;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * This class represents a http client like a browser
 * <p/>
 * to execute request on <a href="http://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
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
     * @return response from <a href="http://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
     */
    String executeRequest(URI uri, Map<String, String> params) throws IOException;

    /**
     * @param uri     uri target
     * @param params  parameters that will be sent in request
     * @param destiny file to store download content
     * @param method  method to use
     * @throws IOException
     */
    String downloadFile(URI uri, File destiny, Map<String, String> params, HttpMethod method) throws IOException;

    /**
     * @param uri    uri target
     * @param params parameters that will be sent in request
     * @param file   file to upload
     * @return response from <a href="http://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
     * @throws IOException
     */
    String uploadFile(URI uri, File file, Map<String, String> params) throws IOException;

    /**
     * Execute a request using form parameters
     *
     * @param uri    uri target
     * @param params parameters that will be sent in request
     * @param method method to use
     * @return response from <a href="http://www.dynamicloud.org" target="_blank">www.dynamicloud.org</a> servers.
     */
    String executeRequest(URI uri, Map<String, String> params, HttpMethod method) throws IOException;

    class Impl {
        public static RecordClient getInstance() {
            return RecordClientImpl.getInstance();
        }
    }
}