package com.kingsley.miaoxin.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kingsley.miaoxin.R;
import com.kingsley.miaoxin.fragment.FindFragment;
import com.kingsley.miaoxin.fragment.FriendFragment;
import com.kingsley.miaoxin.fragment.MessageFragment;
import com.kingsley.miaoxin.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_viewpager)
    ViewPager mainViewpager;
    @BindView(R.id.main_rb_footer_message)
    RadioButton mainRbMessage;
    @BindView(R.id.main_rb_footer_friend)
    RadioButton mainRbFriend;
    @BindView(R.id.main_rb_footer_find)
    RadioButton mainRbFind;
    @BindView(R.id.main_rb_footer_settings)
    RadioButton mainRbSettings;
    @BindView(R.id.main_rg_footer)
    RadioGroup mainRgFooter;
    @BindView(R.id.main_tv_message)
    TextView mainTvMessage;
    @BindView(R.id.main_tv_friend)
    TextView mainTvFriend;
    @BindView(R.id.main_tv_find)
    TextView mainTvFind;
    @BindView(R.id.main_tv_settings)
    TextView mainTvSettings;
    List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleTV.setText("喵信");
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void doBusiness(Context context) {
        addFragments(new MessageFragment(),
                new FriendFragment(),
                new FindFragment(),
                new SettingFragment());
        mainViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton rb = (RadioButton) mainRgFooter.getChildAt(position);
                titleTV.setText(rb.getText());
                mainRgFooter.check(rb.getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mainViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        mainRgFooter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.main_rb_footer_message:
                        mainViewpager.setCurrentItem(0);
                        break;
                    case R.id.main_rb_footer_friend:
                        mainViewpager.setCurrentItem(1);
                        break;
                    case R.id.main_rb_footer_find:
                        mainViewpager.setCurrentItem(2);
                        break;
                    case R.id.main_rb_footer_settings:
                        mainViewpager.setCurrentItem(3);
                        break;
                }
                RadioButton rb = (RadioButton) mainRgFooter.getChildAt(mainViewpager.getCurrentItem());
                titleTV.setText(rb.getText());
            }
        });
    }

    //只有一个页面时可以调用此方法
    private void addFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.main_viewpager, fragment, tag);
        transaction.commit();
    }

    private void addFragments(Fragment... fragments) {
        Collections.addAll(fragmentList, fragments);
    }
}
