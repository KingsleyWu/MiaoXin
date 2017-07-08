package com.kingsley.miaoxin.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.TextKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingsley.miaoxin.R;
import com.kingsley.miaoxin.config.Constant;
import com.kingsley.miaoxin.entity.MyUser;
import com.kingsley.miaoxin.thirdparty.CircleImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.user_info_ci_Avatar)
    CircleImageView userInfoCiAvatar;
    @BindView(R.id.user_info_ed_nickname)
    EditText userInfoEdNickname;
    @BindView(R.id.user_info_iv_nickname_editor)
    ImageView userInfoIvNicknameEditor;
    @BindView(R.id.user_info_ll_edit_nickname_container)
    LinearLayout userInfoLlEditNicknameContainer;
    @BindView(R.id.user_info_tv_username)
    TextView userInfoTvUsername;
    @BindView(R.id.user_info_iv_gender)
    ImageView userInfoIvGender;
    @BindView(R.id.user_info_btn_update)
    Button userInfoBtnUpdate;
    @BindView(R.id.user_info_btn_chat)
    Button userInfoBtnChat;
    @BindView(R.id.user_info_btn_black_room)
    Button userInfoBtnBlackRoom;
    boolean editable;
    String cameraPath;//拍摄头像照片时SD卡的路径
    String avatarUrl;//上传头像照片完毕后，头像照片在服务器上的网址
    private Uri imageUri;
    MyUser user;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showBackBtn();
        String from = baseIntent.getStringExtra(Constant.FROM);
        if (Constant.FRIEND.equals(from)) {
            titleTV.setText("喵友信息");
            userName = baseIntent.getStringExtra(Constant.USERNAME);
            userInfoIvNicknameEditor.setVisibility(View.INVISIBLE);
            userInfoBtnChat.setVisibility(View.VISIBLE);
            userInfoBtnBlackRoom.setVisibility(View.VISIBLE);
            userInfoBtnUpdate.setVisibility(View.GONE);
        } else if (Constant.CURRENTUSER.equals(from)) {
            titleTV.setText("我的资料");
            userInfoLlEditNicknameContainer.setVisibility(View.VISIBLE);
            userName = BmobUser.getCurrentUser(this,MyUser.class).getUsername();
            log(userName);
        }
        setEditTextEditable(userInfoEdNickname, editable);
        userInfoTvUsername.setText(userName);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_user_info;
    }

    @Override
    public void doBusiness(Context context) {
        initView();
    }

    private void initView() {
        BmobQuery<MyUser> query = new BmobQuery<>();
        //查询多条数据使用的方法
        query.addWhereEqualTo(Constant.USERNAME, userName);
        query.findObjects(this, new FindListener<MyUser>() {
            @Override
            public void onSuccess(List<MyUser> list) {
                log(String.valueOf(list.size()));
                for (int i = 0; i < list.size(); i++) {
                    if (userName.contains(list.get(i).getUsername())) {
                        user = list.get(i);
                        setUserInfo();
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                toastAndLog("查询用户信息失败", i, s);
            }
        });
    }

    private void setUserInfo() {
        //根据user中的内容设定界面
        //设定用户头像
        String avatar = user.getAvatar();
        if (TextUtils.isEmpty(avatar)) {
            userInfoCiAvatar.setImageResource(R.drawable.ic_launcher);
        } else {
            Picasso.with(UserInfoActivity.this).load(avatar).into(userInfoCiAvatar);
        }
        //user的昵称
        userInfoEdNickname.setText(user.getNick());
        //user的用户名
        userInfoTvUsername.setText(user.getUsername());
        //user的性别
        if (user.getGender() != null) {
            if (user.getGender()) {
                userInfoIvGender.setImageResource(R.drawable.boy);
            } else {
                userInfoIvGender.setImageResource(R.drawable.girl);
            }
        }else {
            log(user.getGender()+" 性别");
        }
        log(user.toString()+"username ="+user.getUsername());

    }

    //设置EditText可输入和不可输入状态
    private void setEditTextEditable(EditText editText, boolean editable) {
        if (!editable) {
            editText.setKeyListener(null);
        } else {
            editText.setKeyListener(new TextKeyListener(TextKeyListener.Capitalize.CHARACTERS, true));
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
        }
    }

    @OnClick({R.id.user_info_ci_Avatar,R.id.user_info_iv_nickname_editor,
            R.id.user_info_ll_edit_nickname_container, R.id.user_info_btn_update,
            R.id.user_info_btn_chat, R.id.user_info_btn_black_room})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_info_ci_Avatar:
                setAvatar();
                break;
            case R.id.user_info_iv_nickname_editor:
                editable = !editable;
                setEditTextEditable(userInfoEdNickname, editable);
                break;
            case R.id.user_info_ll_edit_nickname_container:
                break;
            case R.id.user_info_btn_update:
                updateInfo();
                break;
            case R.id.user_info_btn_chat:

                break;
            case R.id.user_info_btn_black_room:

                break;
        }
    }

    private void updateInfo() {
        if (avatarUrl != null) {
            user.setAvatar(avatarUrl);
        }
        user.setNick(userInfoEdNickname.getText().toString());
        user.setSessionToken(spUtil.getString(Constant.SessionToken, null));
        user.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                toast("资料更新完成");
            }

            @Override
            public void onFailure(int i, String s) {
                log(i, "资料更新失败: " + s);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //updateInfo();
                        log(user.getObjectId());
                    }
                }, 1500);
            }
        });
    }

    private void setAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                System.currentTimeMillis() + ".png");
        cameraPath = file.getAbsolutePath();
        imageUri = Uri.fromFile(file);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        Intent chooser = Intent.createChooser(intent, "选择头像...");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent2});
        startActivityForResult(chooser, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 101) {
            String filePath = "";
            if (data != null) {
                //图库选图
                //对于部分手机来说，在安卓原生的拍照程序基础上做了修改
                //导致拍照的照片会随着data返回到这里
                Uri uri = data.getData();
                if (uri != null) {
                    if (uri.getPath().equals(imageUri.getPath())) {
                        //拍照
                        //拍照的路径依然是cameraPath
                        filePath = cameraPath;
                    } else {
                        Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                        if (cursor != null) {
                            cursor.moveToNext();
                            filePath = cursor.getString(0);
                            cursor.close();
                            log("图库选图 = filePath = " + filePath);
                        } else {
                            toast("获取图片失败");
                        }
                    }
                } else {
                    Bundle bundle = data.getExtras();
                    //bitmap是拍照回来的照片
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    //TODO 将bitmap存储到SD卡
                }
            } else {
                filePath = cameraPath;
                log("data=null = filePath = " + filePath);
            }

            final BmobFile bmobFile = new BmobFile(new File(filePath));
            bmobFile.uploadblock(this, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    //获取上传到服务器的图片地址
                    avatarUrl = bmobFile.getFileUrl(UserInfoActivity.this);
                    log("avatarUrl=" + avatarUrl);
                    //把图片显示到控件中
                    Picasso.with(UserInfoActivity.this).load(avatarUrl).into(userInfoCiAvatar);
                }

                @Override
                public void onFailure(int i, String s) {
                    toastAndLog("上传头像失败", i, s);
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
