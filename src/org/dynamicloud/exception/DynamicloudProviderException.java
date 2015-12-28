package org.dynamicloud.exception;

/**
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/24/15
 **/
public class DynamicloudProviderException extends Exception {
    /**
     * Default constructor
     *
     * @param s a description of the occurred error.
     */
    public DynamicloudProviderException(String s) {
        super(s);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}