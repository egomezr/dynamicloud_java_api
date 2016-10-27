package org.dynamicloud.exception;

import org.dynamicloud.net.http.HttpStatus;

/**
 * This is a Service exception to indicate a specific situation.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 2015-08-22
 *
 **/
public class DynamiCloudServiceException extends Exception {
    /**
     * Default super constructor
     *
     * @param s a description of the occurred error.
     */
    public DynamiCloudServiceException(String s) {
        super(s);
    }
}