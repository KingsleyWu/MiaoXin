package com.kingsley.miaoxin.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingsley.miaoxin.R;
import com.kingsley.miaoxin.config.Constant;
import com.kingsley.miaoxin.entity.MyUser;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.login_iv_avatar)
    ImageView loginIvAvatar;
    @BindView(R.id.login_et_username)
    EditText loginEtUsername;
    @BindView(R.id.login_et_password)
    EditText loginEtPassword;
    @BindView(R.id.login_btn_login)
    Button loginBtnLogin;
    @BindView(R.id.Login_tv_noLogonUser)
    TextView LoginTvNoLogonUser;
    @BindView(R.id.Login_tv_logon)
    TextView LoginTvLogon;
    @BindView(R.id.login_ll_logon)
    LinearLayout loginLlLogon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleTV.setText("登录");
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    public void doBusiness(Context context) {

    }

    @OnClick({R.id.login_btn_login, R.id.Login_tv_noLogonUser,R.id.Login_tv_logon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:
                login();
                break;
            case R.id.Login_tv_noLogonUser:
            case R.id.Login_tv_logon:
                jumpTo(LogonActivity.class,false,false);
                break;
        }
    }

    private void login() {
        if (isEmpty(loginEtUsername,loginEtPassword)){
            return;
        }
        final MyUser user = new MyUser();
        final String username = loginEtUsername.getText().toString();
        final String password = loginEtPassword.getText().toString();
        user.setUsername(username);
        user.setPassword(password);
        user.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                spUtil.putString(Constant.USERNAME,username);
                spUtil.putString(Constant.PASSWORD,password);
                spUtil.putString(Constant.SessionToken,user.getSessionToken());
                jumpTo(MainActivity.class,true,true);
                log(spUtil.getString(Constant.USERNAME,null));
            }

            @Override
            public void onFailure(int i, String s) {
                //根据不同的i,尽量给出详细的提示
                switch (i) {
                    case 101:
                        toast("用户名或密码错误");
                        break;
                    default:
                        toastAndLog("登录失败", i, s);
                        break;
                }
            }
        });
    }
}
