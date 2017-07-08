package com.kingsley.miaoxin.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kingsley.miaoxin.config.Constant;

/**
 * class name : MiaoXin
 * author : Kingsley
 * created date : on 2017/7/5 11:36
 * file change date : on 2017/7/5 11:36
 * version: 1.0
 */

public class SPUtil {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    //通过构造器重载，以不同的方式来获得偏好设置文件

    public SPUtil(Context context, String name){
        sp = context.getSharedPreferences(name,Context.MODE_PRIVATE);
    }

    public SPUtil(Context context){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sp.edit();
    }

    public boolean isFirst(){
        return sp.getBoolean(Constant.FIRST,true);
    }

    public void setFirst(boolean flag){
        editor.putBoolean(Constant.FIRST,flag);
        editor.apply();
    }

    public void setBoolean(String key,boolean flag){
        editor.putBoolean(key,flag);
        editor.apply();
    }

    public boolean getBoolean(String key){
        return sp.getBoolean(key,true);
    }

    public String getString(String key,String value){
        return sp.getString(key,value);
    }

    public void putString(String key,String value){
        editor.putString(key,value);
        editor.apply();
    }

    public void remove(String... keys){
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
    }
}
