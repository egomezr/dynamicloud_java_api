package org.dynamicloud.bean;

import org.dynamicloud.api.BoundInstance;
import org.dynamicloud.api.annotation.Bind;

/**
 * This class represents a BlogRecord in Blog Model.
 * Implements BoundInstance to be used in DynamicProvider methods.
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 10/29/15
 **/
public class BlogRecord implements BoundInstance {
    private Long id;
    private Long creatorId;
    private String blogName;
    private String blogDescription;
    private String blogCategory;
    private String blogCreateDate;

    @Override
    public Number getRecordId() {
        return this.id;
    }

    @Override
    @Bind(field = "rid")
    public void setRecordId(Number bid) {
        this.id = bid.longValue();
    }

    public Long getCreatorId() {
        return creatorId;
    }

    @Bind(field = "creatrid")
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getBlogName() {
        return blogName;
    }

    @Bind(field = "blogname")
    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getBlogDescription() {
        return blogDescription;
    }

    @Bind(field = "blogdesc")
    public void setBlogDescription(String blogDescription) {
        this.blogDescription = blogDescription;
    }

    public String getBlogCategory() {
        return blogCategory;
    }

    @Bind(field = "blogcat")
    public void setBlogCategory(String blogCategory) {
        this.blogCategory = blogCategory;
    }

    public String getBlogCreateDate() {
        return blogCreateDate;
    }

    @Bind(field = "createdt")
    public void setBlogCreateDate(String blogCreateDate) {
        this.blogCreateDate = blogCreateDate;
    }
}