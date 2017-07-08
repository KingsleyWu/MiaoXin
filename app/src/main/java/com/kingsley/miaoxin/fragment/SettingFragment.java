package com.kingsley.miaoxin.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingsley.miaoxin.R;
import com.kingsley.miaoxin.config.Constant;
import com.kingsley.miaoxin.ui.LoginActivity;
import com.kingsley.miaoxin.ui.UserInfoActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingFragment extends BaseFragment {

    @BindView(R.id.settings_tv_my)
    TextView settingsTvMy;
    @BindView(R.id.settings_iv_my_editor)
    ImageView settingsIvMyEditor;
    @BindView(R.id.settings_tv_black_room)
    TextView settingsTvBlackRoom;
    @BindView(R.id.settings_iv_black_room_editor)
    ImageView settingsIvBlackRoomEditor;
    @BindView(R.id.settings_tv_notification)
    TextView settingsTvNotification;
    @BindView(R.id.settings_iv_notification_sw)
    CheckBox settingsIvNotificationSw;
    @BindView(R.id.settings_tv_sound)
    TextView settingsTvSound;
    @BindView(R.id.settings_iv_sound_sw)
    CheckBox settingsIvSoundSw;
    @BindView(R.id.settings_tv_black_room_editor)
    TextView settingsTvBlackRoomEditor;
    @BindView(R.id.settings_iv_shake_sw)
    CheckBox settingsIvShakeSw;
    @BindView(R.id.settings_logout)
    Button settingsLogout;
    private Boolean isNotification;
    private Boolean isSound;
    private Boolean isShake;

    public SettingFragment() {

    }
    @Override
    public View createMyView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void doBusiness() {
        initSwitch();
        settingsTvMy.setText(user.getUsername());
    }

    @OnClick({R.id.settings_tv_my,
            R.id.settings_iv_my_editor,
            R.id.settings_tv_black_room,
            R.id.settings_iv_black_room_editor,
            R.id.settings_tv_notification,
            R.id.settings_iv_notification_sw,
            R.id.settings_tv_sound,
            R.id.settings_iv_sound_sw,
            R.id.settings_tv_black_room_editor,
            R.id.settings_iv_shake_sw,
            R.id.settings_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.settings_tv_my:
                break;
            case R.id.settings_iv_my_editor:
                Intent intent = new Intent(baseActivity, UserInfoActivity.class);
                //Constant.CURRENTUSER
                intent.putExtra(Constant.FROM,Constant.CURRENTUSER);
                jumpTo(intent,false,true);
                break;
            case R.id.settings_tv_black_room:
                break;
            case R.id.settings_iv_black_room_editor:
                break;
            case R.id.settings_tv_notification:
                break;
            case R.id.settings_iv_notification_sw:
                spUtil.setBoolean(Constant.NOTIFICATION,!isNotification);
                break;
            case R.id.settings_tv_sound:
                break;
            case R.id.settings_iv_sound_sw:
                spUtil.setBoolean(Constant.SOUND,!isSound);
                break;
            case R.id.settings_tv_black_room_editor:
                break;
            case R.id.settings_iv_shake_sw:
                spUtil.setBoolean(Constant.SHAKE,!isShake);
                break;
            case R.id.settings_logout:
                spUtil.remove(Constant.USERNAME,Constant.PASSWORD);
                jumpTo(LoginActivity.class,true,true);
                break;
        }
    }

    private void initSwitch() {
        isNotification = spUtil.getBoolean(Constant.NOTIFICATION);
        settingsIvNotificationSw.setChecked(isNotification);
        isSound = spUtil.getBoolean(Constant.SOUND);
        settingsIvSoundSw.setChecked(isSound);
        isShake = spUtil.getBoolean(Constant.SHAKE);
        settingsIvShakeSw.setChecked(isShake);
    }
}
