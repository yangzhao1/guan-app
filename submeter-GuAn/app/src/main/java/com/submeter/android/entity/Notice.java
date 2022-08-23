package com.submeter.android.entity;

/**
 * Created by yangzhao on 2019/4/8.
 */

public class Notice {

    /**
     * id
     */
    private String id;

    /**
     * title
     */
    private String title;

    /**
     * title
     */
    private String name;

    /**
     * content
     */
    private String content;

    /**
     * content
     */
    private String state;

    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
