package com.kingsley.miaoxin.entity;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> loveUsers;

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

    public List<String> getLoveUsers() {
        if (loveUsers == null) {
            loveUsers = new ArrayList<>();
        }
        return loveUsers;
    }

    public void setLoveUsers(List<String> loveUser) {
        this.loveUsers = loveUser;
    }
}
