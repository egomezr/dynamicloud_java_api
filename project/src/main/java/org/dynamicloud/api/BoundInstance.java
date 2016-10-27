package org.dynamicloud.api;

import org.dynamicloud.api.annotation.Bind;

/**
 * This interface declares the required methods for a bound instance
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/30/15
 **/
public interface BoundInstance {
    /**
     * Should return the record id
     * @return record id
     */
    Number getRecordId();

    /**
     * Sets a new record id
     * @param newRid new record id
     */
    @Bind(field = "rid")
    void setRecordId(Number newRid);
}