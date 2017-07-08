package com.kingsley.miaoxin.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kingsley.miaoxin.R;
import com.kingsley.miaoxin.config.Constant;
import com.kingsley.miaoxin.entity.MyUser;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.listener.SaveListener;

public class LogonActivity extends BaseActivity {

    @BindView(R.id.logon_et_username)
    EditText mLogonEtUsername;
    @BindView(R.id.logon_et_password)
    EditText mLogonEtPassword;
    @BindView(R.id.logon_et_re_password)
    EditText mLogonEtRePassword;
    @BindView(R.id.logon_rg_gender)
    RadioGroup mLogonRgGender;
    @BindView(R.id.logon_rb_gender_boy)
    RadioButton mLogonRbGenderBoy;
    @BindView(R.id.logon_rb_gender_girl)
    RadioButton mLogonRbGenderGirl;
    @BindView(R.id.logon_btn_logon)
    Button mLogonBtnLogon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleTV.setText("注册");
        showBackBtn();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_logon;
    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick(R.id.logon_btn_logon)
    public void onClicked(View view) {
        if (isEmpty(mLogonEtUsername,mLogonEtPassword,mLogonEtRePassword)){
            return;
        }
        //3)构建实体类(MyUser)对象
        final MyUser user = new MyUser();
        final String password = mLogonEtPassword.getText().toString();
        final String username = mLogonEtUsername.getText().toString();
        user.setUsername(username);
        //是否MD5加密？取决于同学自己的设计
        user.setPassword(password);
        boolean gender = true;
        if (mLogonRgGender.getCheckedRadioButtonId() == R.id.logon_rb_gender_girl) {
            gender = false;
        }
        user.setGender(gender);
        //进行注册
        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                //进行登录
                user.login(LogonActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        //登录成功
                        //跳转界面，跳转到MainActivity
                        spUtil.putString(Constant.USERNAME,username);
                        spUtil.putString(Constant.PASSWORD,password);
                        spUtil.putString(Constant.SessionToken,user.getSessionToken());
                        jumpTo(MainActivity.class, true,true);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        toastAndLog("登录失败", i, s);
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                switch (i) {
                    case 202:
                        toast("用户名重复");
                        break;

                    default:
                        toastAndLog("注册用户失败稍后重试", i, s);
                        break;
                }
            }
        });
    }
}
