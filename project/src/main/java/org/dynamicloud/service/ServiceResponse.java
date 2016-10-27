package org.dynamicloud.service;

/**
 * This class represents a response from Dynamicloud servers.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/25/15
 **/
public class ServiceResponse {
    private String response;

    /**
     * Builds this response with a string from Dynamicloud servers
     *
     * @param response response from Dynamicloud servers
     */
    public ServiceResponse(String response) {
        this.response = response;
    }

    /**
     * gets the current response
     *
     * @return the current response
     */
    public String getResponse() {
        return response;
    }
}