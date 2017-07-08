package com.kingsley.miaoxin.entity;

import cn.bmob.v3.BmobUser;

/**
 * class name : MiaoXin
 * author : Kingsley
 * created date : on 2017/7/5 10:41
 * file change date : on 2017/7/5 10:41
 * version: 1.0
 */

public class User extends BmobUser {
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 性别
     */
    private Boolean gender;
    /**
     * 昵称
     */
    private String nickname;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
