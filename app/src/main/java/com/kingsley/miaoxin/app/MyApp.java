package com.kingsley.miaoxin.app;

import android.app.Activity;
import android.app.Application;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import com.kingsley.miaoxin.config.Constant;
import com.kingsley.miaoxin.entity.MyUser;
import com.kingsley.miaoxin.util.SPUtil;
import cn.bmob.v3.Bmob;

/**
 * class name : MiaoXin
 * author : Kingsley
 * created date : on 2017/7/5 09:40
 * file change date : on 2017/7/5 09:40
 * version: 1.0
 */

public class MyApp extends Application {
    private static final String TAG = "MyApp";
    public static MyApp context;
    public static final String ACTION = "com.kingsley.action_new_post";
    public static MediaPlayer player;
    public static SPUtil spUtil;
    public static MyUser user;
    //温吉龙  c2e5cb174a15eb48ca924da257cb476c
    //6c8bfbefc54fc9aab993a30aefa34e69
    //de916d032a6b38d808623bb0e1c49629
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        spUtil = new SPUtil(this);
        Bmob.initialize(this, Constant.BMOBKEY);

        //BmobInstallation.getCurrentInstallation(this).save();
        // 启动接受服务器推送服务
        //BmobPush.startWork(this);
        //registerActivityLifecycleCallbacks(mCallbacks);
    }

    private ActivityLifecycleCallbacks mCallbacks = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.d(TAG, "onActivityCreated() called with: activity = [" + activity + "], savedInstanceState = [" + savedInstanceState + "]");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d(TAG, "onActivityStarted() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.d(TAG, "onActivityResumed() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.d(TAG, "onActivityPaused() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d(TAG, "onActivityStopped() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.d(TAG, "onActivitySaveInstanceState() called with: activity = [" + activity + "], outState = [" + outState + "]");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d(TAG, "onActivityDestroyed() called with: activity = [" + activity + "]");
        }
    };
}
