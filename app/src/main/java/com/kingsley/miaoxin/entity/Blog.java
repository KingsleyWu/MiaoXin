package com.kingsley.miaoxin.entity;

import cn.bmob.v3.BmobObject;

/**
 * class name : MiaoXin
 * author : Kingsley
 * created date : on 2017/7/6 09:30
 * file change date : on 2017/7/6 09:30
 * version: 1.0
 */

public class Blog extends BmobObject {
    private MyUser author;
    private String content;
    private String imgUrls;
    private Integer love;

    public MyUser getAuthor() {
        return author;
    }

    public void setAuthor(MyUser author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }

    public Integer getLove() {
        return love;
    }

    public void setLove(Integer love) {
        this.love = love;
    }
}
