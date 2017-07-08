package com.kingsley.miaoxin.ui;

import android.content.Context;
import android.os.Bundle;

import com.kingsley.miaoxin.R;
import com.kingsley.miaoxin.config.Constant;
import com.kingsley.miaoxin.entity.MyUser;

import cn.bmob.v3.listener.SaveListener;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_launch;
    }

    @Override
    public void doBusiness(Context context) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                autoLogin();
            }
        },1500);
    }

    private void autoLogin() {
        final String username = spUtil.getString(Constant.USERNAME, null);
        final String password = spUtil.getString(Constant.PASSWORD, null);

        if (username != null && password != null) {
            final MyUser user = new MyUser();
            user.setUsername(username);
            user.setPassword(password);
            user.login(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    //跳转至 MainActivity
                    spUtil.putString(Constant.SessionToken,user.getSessionToken());
                    jumpTo(MainActivity.class,true,false);
                    log("onSuccess  SessionToken ="+user.getSessionToken());
                }
                @Override
                public void onFailure(int i, String s) {
                    //登录失败
                    //跳转至 LoginActivity
                    jumpTo(LoginActivity.class,true,false);
                    log(i,s);
                }
            });
        } else {//从来没有登录过
            //跳转至 LoginActivity
            jumpTo(LoginActivity.class,true,false);
            log("从来没有登录过");
        }
    }
}
