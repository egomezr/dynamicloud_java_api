package org.dynamicloud.junit.bean;

import org.dynamicloud.api.BoundInstance;
import org.dynamicloud.api.annotation.Bind;

/**
 * This class represents the result of a join clause execution
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 11/7/15
 **/
public class JoinResultBean implements BoundInstance {
    private String country;
    private String birthDate;

    public String getCountry() {
        return country;
    }

    @Bind(field = "country")
    public void setCountry(String country) {
        this.country = country;
    }

    public String getBirthDate() {
        return birthDate;
    }

    @Bind(field = "birthdate")
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Should return the record id
     *
     * @return record id
     */
    @Override
    public Number getRecordId() {
        return null;
    }

    /**
     * Sets a new record id
     *
     * @param newRid new record id
     */
    @Override
    public void setRecordId(Number newRid) {

    }
}