package org.dynamicloud.bean;

import org.dynamicloud.api.BoundInstance;
import org.dynamicloud.api.annotation.Bind;

/**
 * This class represents a post in model
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 10/31/15
 **/
public class PostRecord implements BoundInstance {
    private Long id;
    private Long creatorId;
    private Long blogId;
    private String postContent;
    private String postTitle;
    private String postCreateDate;

    @Override
    public Number getRecordId() {
        return id;
    }

    @Override
    @Bind(field = "rid")
    public void setRecordId(Number number) {
        this.id = number.longValue();
    }

    public Long getCreatorId() {
        return creatorId;
    }

    @Bind(field = "creatrid")
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getBlogId() {
        return blogId;
    }

    @Bind(field = "blogid")
    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getPostContent() {
        return postContent;
    }

    @Bind(field = "postcont")
    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostTitle() {
        return postTitle;
    }

    @Bind(field = "posttitl")
    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostCreateDate() {
        return postCreateDate;
    }

    @Bind(field = "createdt")
    public void setPostCreateDate(String postCreateDate) {
        this.postCreateDate = postCreateDate;
    }
}