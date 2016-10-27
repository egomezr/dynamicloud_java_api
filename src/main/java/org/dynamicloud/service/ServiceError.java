package org.dynamicloud.service;

/**
 * This class represents an error from Dynamicloud servers
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/26/15
 **/
public class ServiceError {
    private String message;
    private String code;

    /**
     * Builds an error using message and code sent from Dynamicloud servers
     * @param message message from Dynamicloud servers
     * @param code code from Dynamicloud servers
     */
    public ServiceError(String message, String code) {
        this.message = message;
        this.code = code;
    }

    /**
     * Gets the current message
     *
     * @return the current message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the current code
     *
     * @return the current code
     */
    public String getCode() {
        return code;
    }
}