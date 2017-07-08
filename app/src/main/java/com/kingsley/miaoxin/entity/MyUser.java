package com.kingsley.miaoxin.entity;

import cn.bmob.v3.BmobUser;

/**
 * class name : MiaoXin
 * author : Kingsley
 * created date : on 2017/7/5 15:10
 * file change date : on 2017/7/5 15:10
 * version: 1.0
 */

public class MyUser extends BmobUser {
    //性别，位置，拼音名称和拼音首字母
    private Boolean gender;//true 男生  false 女生
    private String avatar;//存储头像
    private String nick;//昵称

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "MyUser{" +
                "gender=" + gender +
                ", avatar='" + avatar + '\'' +
                ", nick='" + nick + '\'' +
                '}';
    }
}
