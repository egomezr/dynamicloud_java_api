package org.dynamicloud.junit.bean;

import org.dynamicloud.api.BoundInstance;
import org.dynamicloud.api.annotation.Bind;

/**
 *
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/27/15
 **/
public class ModelFields implements BoundInstance {
    private Number rid;
    private String nameCount;
    private String username;
    private String[] password;
    private String email;
    private String country;
    private String name;
    private String birthdat;
    private String auxiliarFile;
    private Number numberResult;
    private Number ageField;
    private String stringResult;
    private String [] cities;
    private String photo;

    @Override
    public Number getRecordId() {
        return rid;
    }

    @Override
    @Bind(field = "rid")
    public void setRecordId(Number newRid) {
        this.rid = newRid;
    }

    public String getUsername() {
        return username;
    }

    @Bind(field = "username")
    public void setUsername(String username) {
        this.username = username;
    }

    public String[] getPassword() {
        return password;
    }

    @Bind(field = "password")
    public void setPassword(String[] password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    @Bind(field = "email")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    @Bind(field = "country")
    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    @Bind(field = "name")
    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdat() {
        return birthdat;
    }

    @Bind(field = "birthdat")
    public void setBirthdat(String birthdat) {
        this.birthdat = birthdat;
    }

    public String getAuxiliarFile() {
        return auxiliarFile;
    }

    @Bind(field = "fieldtwo")
    public void setAuxiliarFile(String auxiliarFile) {
        this.auxiliarFile = auxiliarFile;
    }

    public String getNameCount() {
        return nameCount;
    }

    @Bind(field = "nameCount")
    public void setNameCount(String nameCount) {
        this.nameCount = nameCount;
    }

    public String[] getCities() {
        return cities;
    }

    @Bind(field = "cities")
    public void setCities(String[] cities) {
        this.cities = cities;
    }

    public Number getNumberResult() {
        return numberResult;
    }

    @Bind(field = "number")
    public void setNumberResult(Number numberResult) {
        this.numberResult = numberResult;
    }

    public String getStringResult() {
        return stringResult;
    }

    @Bind(field = "result")
    public void setStringResult(String stringResult) {
        this.stringResult = stringResult;
    }

    public Number getAgeField() {
        return ageField;
    }

    @Bind(field = "agefield")
    public void setAgeField(Number ageField) {
        this.ageField = ageField;
    }

    public String getPhoto() {
        return photo;
    }

    @Bind(field = "photo")
    public void setPhoto(String photo) {
        this.photo = photo;
    }
}