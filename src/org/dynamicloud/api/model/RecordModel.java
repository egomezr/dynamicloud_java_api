package org.dynamicloud.api.model;

/**
 * This class represents a Model in <b>Dynamicloud</b>.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/25/15
 **/
public class RecordModel {
    private Long id;
    private String name;
    private String description;
    private Class boundClass;

    /**
     * Constructs a model using its ID without Mapper
     * @param id model id
     */
    public RecordModel(Long id) {
        this(id, null);
    }

    /**
     * Constructs a model using its ID with Mapper
     * @param id model id
     * @param boundClass is the class with attributes that matches with fields in Dynamicloud
     */
    public RecordModel(Long id, Class boundClass) {
        this.id = id;
        this.boundClass = boundClass;
    }

    /**
     * gets the model id
     *
     * @return a long id
     */
    public Long getId() {
        return id;
    }

    /**
     * gets the current mapper
     *
     * @return current mapper
     */
    public Class getBoundClass() {
        return boundClass;
    }

    /**
     * Sets the bound class to use to bind local attributes with model's fields
     *
     * @param boundClass bound class
     */
    public void setBoundClass(Class boundClass) {
        this.boundClass = boundClass;
    }

    /**
     * Sets a model id
     * @param id model id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the model's name
     * @return model's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a model's name
     * @param name model's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the model's description
     * @return model's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the model's description
     * @param description model's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RecordModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", boundClass=" + boundClass +
                '}';
    }
}