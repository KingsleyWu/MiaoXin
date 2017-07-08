package com.kingsley.miaoxin.ui;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kingsley.miaoxin.app.MyApp;
import com.kingsley.miaoxin.config.Constant;
import com.kingsley.miaoxin.entity.MyUser;
import com.kingsley.miaoxin.util.SPUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;

/**
 * class name : MiaoXin
 * author : Kingsley
 * created date : on 2017/7/5 11:53
 * file change date : on 2017/7/5 11:53
 * version: 1.0
 */

public abstract class BaseActivity extends AppCompatActivity{
    SPUtil spUtil;
    /**
     * 是否全屏
     */
    boolean isFullScreen     = false;
    /**
     * 是否沉浸状态栏
     */
    boolean isSteepStatusBar = false;
    /**
     * 当前Activity渲染的视图View
     */

    Toast toast;

    TextView titleTV;

    Handler handler;
    private Unbinder unbinder;
    Intent baseIntent;
    MyUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            spUtil = MyApp.spUtil;
            baseIntent = getIntent();
            user = BmobUser.getCurrentUser(this,MyUser.class);
            if (isFullScreen) {
                this.getWindow().setFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                requestWindowFeature(Window.FEATURE_NO_TITLE);
            }
            if (isSteepStatusBar) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                }
            }
            setContentView(getLayoutID());
            unbinder = ButterKnife.bind(this);

            handler = new Handler();

            toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

            ActionBar ab = getSupportActionBar();
            titleTV = new TextView(this);
            titleTV.setText("Title");
            titleTV.setTextColor(Color.WHITE);
            titleTV.setTextSize(20);
            titleTV.setGravity(Gravity.CENTER);
            assert ab != null;
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER);
            ab.setCustomView(titleTV,lp);
            ab.setDisplayShowTitleEnabled(false);

            doBusiness(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract int getLayoutID();

    protected  void showBackBtn(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    //关于toast和log
    public void toast(String text) {
        if (!TextUtils.isEmpty(text)) {
            toast.setText(text);
            toast.show();
        }
    }

    public void log(String log) {
        if (Constant.DEBUG) {
            Log.d("TAG", "从" + this.getClass().getSimpleName() + "打印的日志：" + log);
        }
    }

    public void log(int code, String message) {
        String log = "错误信息：" + code + ":" + message;
        log(log);
    }

    public void toastAndLog(String text, String log) {
        toast(text);
        log(log);
    }

    public void toastAndLog(String text, int code, String message) {
        toast(text);
        log(code, message);
    }

    //关于界面跳转
    public void jumpTo(Class<?> clazz, boolean isFinish, boolean isAnimation) {
        Intent intent = new Intent(this, clazz);
        if (isAnimation) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }else {
                startActivity(intent);
            }
        }else {
            startActivity(intent);
        }
        if (isFinish) {

            finish();
        }
    }

    public void jumpTo(Intent intent, boolean isFinish, boolean isAnimation) {
        if (isAnimation) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            }else {
                startActivity(intent);
            }
        }else {
            startActivity(intent);
        }
        if (isFinish) {
            finish();
        }
    }


    /**
     * 是用来判定EditText是否都输入了内容
     *
     * @return true 意味着EditText...有未输入内容
     * false 意味着所有的EditText都输入了内容
     */

    public boolean isEmpty(EditText... ets) {

        for (EditText et : ets) {
            String text = et.getText().toString();
            if (TextUtils.isEmpty(text)) {
                //et.setError("请输入完整！");
                SpannableString ss = new SpannableString("请输入完整！");
                //你给ss使用哪一种span效果
                ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 3, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                //ss.setSpan(new BackgroundColorSpan(Color.BLACK), 0, 6, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                //ss.setSpan(new ImageSpan(this, R.drawable.boy), 5, 6, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                et.setError(ss);
                return true;
            }
        }
        return false;
    }

    //点击空白区域收软件盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context
                .INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                    0);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 业务操作
     *
     * @param context 上下文
     */
    public abstract void doBusiness(final Context context);

    /**
     * 设置是否全屏
     *
     * @param isFullScreen 是否全屏
     */
    public void setFullScreen(final boolean isFullScreen) {
        this.isFullScreen = isFullScreen;
    }

    /**
     * 设置是否沉浸状态栏
     *
     * @param isSteepStatusBar 是否沉浸状态栏
     */
    public void setSteepStatusBar(final boolean isSteepStatusBar) {
        this.isSteepStatusBar = isSteepStatusBar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
