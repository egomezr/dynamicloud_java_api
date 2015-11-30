package org.dynamicloud.junit.bean;

import org.dynamicloud.api.BoundInstance;
import org.dynamicloud.api.annotation.Bind;

import java.sql.Date;

/**
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 11/28/15
 **/
public class DateBean implements BoundInstance {
    private Date date;
    private Long id;

    /**
     * Should return the record id
     *
     * @return record id
     */
    @Override
    public Number getRecordId() {
        return this.id;
    }

    /**
     * Sets a new record id
     *
     * @param newRid new record id
     */
    @Override
    @Bind(field = "rid")
    public void setRecordId(Number newRid) {
        this.id = newRid.longValue();
    }

    public Date getDate() {
        return date;
    }

    @Bind(field = "datefie")
    public void setDate(Date date) {
        this.date = date;
    }
}