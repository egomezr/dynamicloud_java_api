package org.dynamicloud.bean;

import org.dynamicloud.api.BoundInstance;
import org.dynamicloud.api.annotation.Bind;

/**
 * This class represents a user in UserModel.
 * Implements BoundInstance to be used in DynamicProvider methods.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 10/28/15
 **/
public class UserRecord implements BoundInstance {
    private String username;
    private String email;
    private Long id;

    public String getUsername() {
        return username;
    }

    @Bind(field = "username")
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    @Bind(field = "email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Should return the record id
     *
     * @return record id
     */
    @Override
    public Number getRecordId() {
        return id;
    }

    /**
     * Sets a new record id
     *
     * @param number new record id
     */
    @Override
    @Bind(field = "rid")
    public void setRecordId(Number number) {
        id = number.longValue();
    }
}