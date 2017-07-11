package com.kingsley.miaoxin.entity;

import cn.bmob.v3.BmobObject;

/**
 * class name : MiaoXin
 * author : Kingsley
 * created date : on 2017/7/10 09:24
 * file change date : on 2017/7/10 09:24
 * version: 1.0
 */

public class Comment extends BmobObject {
    private String username;
    private String content;
    private String blogId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }
}
