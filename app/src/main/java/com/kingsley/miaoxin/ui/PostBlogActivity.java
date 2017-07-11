package com.kingsley.miaoxin.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingsley.miaoxin.R;
import com.kingsley.miaoxin.entity.Blog;
import com.kingsley.miaoxin.entity.MyUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class PostBlogActivity extends BaseActivity {

    @BindView(R.id.post_blog_ed_content)
    EditText postBlogEdContent;
    @BindView(R.id.post_blog_ll_ivContainer)
    LinearLayout postBlogLLIvContainer;
    @BindView(R.id.post_blog_iv_iv1)
    ImageView postBlogIvIv1;
    @BindView(R.id.post_blog_iv_del_iv1)
    ImageView postBlogIvDelIv1;
    @BindView(R.id.post_blog_rl_iv1)
    RelativeLayout postBlogRlIv1;
    @BindView(R.id.post_blog_iv_iv2)
    ImageView postBlogIvIv2;
    @BindView(R.id.post_blog_iv_del_iv2)
    ImageView postBlogIvDelIv2;
    @BindView(R.id.post_blog_rl_iv2)
    RelativeLayout postBlogRlIv2;
    @BindView(R.id.post_blog_iv_iv3)
    ImageView postBlogIvIv3;
    @BindView(R.id.post_blog_iv_del_iv3)
    ImageView postBlogIvDelIv3;
    @BindView(R.id.post_blog_rl_iv3)
    RelativeLayout postBlogRlIv3;
    @BindView(R.id.post_blog_iv_iv4)
    ImageView postBlogIvIv4;
    @BindView(R.id.post_blog_iv_del_iv4)
    ImageView postBlogIvDelIv4;
    @BindView(R.id.post_blog_rl_iv4)
    RelativeLayout postBlogRlIv4;
    @BindView(R.id.post_blog_tv_blogIv_count)
    TextView postBlogTvBlogIvCount;
    @BindView(R.id.post_blog_tv_progressbar)
    TextView postBlogTvProgressbar;
    @BindView(R.id.post_blog_progressbar)
    ProgressBar postBlogProgressbar;
    @BindView(R.id.post_blog_ll_progressbar)
    LinearLayout postBlogLlProgressbar;
    @BindView(R.id.post_blog_ib_add)
    ImageButton postBlogIbAdd;
    @BindView(R.id.post_blog_ib_picture)
    ImageButton postBlogIbPicture;
    @BindView(R.id.post_blog_ib_camera)
    ImageButton postBlogIbCamera;
    @BindView(R.id.post_blog_ib_location)
    ImageButton postBlogIbLocation;
    @BindView(R.id.post_blog_ll_btnContainer)
    LinearLayout postBlogLLBtnContainer;

    List<ImageView> blogImages;//保存blog发送的图片
    List<RelativeLayout> blogRLImages;

    boolean isExpanded;//默认时为false
    boolean isPosting;//是否有blog正处于上传过程中

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleTV.setText("新建喵迹");
        showBackBtn();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_post_blog;
    }

    @Override
    public void doBusiness(Context context) {
        blogImages = new ArrayList<>();
        blogImages.add(postBlogIvIv1);
        blogImages.add(postBlogIvIv2);
        blogImages.add(postBlogIvIv3);
        blogImages.add(postBlogIvIv4);

        blogRLImages = new ArrayList<>();
        blogRLImages.add(postBlogRlIv1);
        blogRLImages.add(postBlogRlIv2);
        blogRLImages.add(postBlogRlIv3);
        blogRLImages.add(postBlogRlIv4);

        postBlogLLIvContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //调整postBlogLLIvContainer中四个RelativeLayout的尺寸
                int width = postBlogLLIvContainer.getWidth();
                int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
                int size = (width - margin * 5) / 4;
                for (int i = 0; i < 4; i++) {
                    View childView = postBlogLLIvContainer.getChildAt(i);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
                    if (i == 0) {
                        params.setMargins(margin, 0, margin, 0);
                    } else {
                        params.setMargins(0, 0, margin, 0);
                    }
                    childView.setLayoutParams(params);
                }
                //注销监听器
                postBlogLLIvContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //让postBlogLLIvContainer按设定好的尺寸,重新申请摆放位置
                postBlogLLIvContainer.requestLayout();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu_send_blog, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sendBlog) {
            if (!isEmpty(postBlogEdContent)) {
                postBlog();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void postBlog() {
        if (blogRLImages.get(0).getVisibility() == View.INVISIBLE){
            postBlog("");
            return;
        }
        //要上传的图片文件的路径数组
        List<String> paths = new ArrayList<>();
        for (int i = 0; i < blogRLImages.size(); i++) {
            if (blogRLImages.get(i).getVisibility() == View.VISIBLE) {
                paths.add((String) blogImages.get(i).getTag());
            }
        }
        final String[] filePaths = paths.toArray(new String[paths.size()]);
        //log("postBlog  ="+ Arrays.toString(filePaths));
        postBlogLlProgressbar.setVisibility(View.VISIBLE);

        BmobFile.uploadBatch(this, filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                //上传使用的是单个文件上传,所以返回时集合的size是从1到所要传递的图片的数量递增
                if (list1.size() == filePaths.length) {
                    StringBuilder builder = new StringBuilder();
                    for (String s : list1) {
                        builder.append(s).append("&");
                    }
                    postBlogProgressbar.setVisibility(View.INVISIBLE);
                    postBlog(builder.substring(0,builder.length()-1));
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
                postBlogProgressbar.setProgress(i3);
                postBlogTvProgressbar.setText(""+i3+"%");
                isPosting = true;
            }

            @Override
            public void onError(int i, String s) {
                log(i,s);
            }
        });
    }

    private void postBlog(String filePath) {
        Blog blog = new Blog();
        blog.setAuthor(BmobUser.getCurrentUser(this, MyUser.class));
        blog.setContent(postBlogEdContent.getText().toString());
        blog.setImgUrls(filePath);
        blog.setLoveUsers(new ArrayList<String>());
        blog.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                isPosting = false;
                toast("喵迹发布成功...");
                if (postBlogEdContent != null) {
                    postBlogEdContent.setText("");
                    //隐藏所有图片
                    for (int i = 0; i < blogImages.size(); i++) {
                        blogRLImages.get(i).setVisibility(View.INVISIBLE);
                    }
                    postBlogTvBlogIvCount.setText("");
                    finish();
                }
            }

            @Override
            public void onFailure(int i, String s) {
                isPosting = false;
                toastAndLog("博客发布失败", i, s);
            }
        });
    }

    @OnClick({R.id.post_blog_iv_del_iv1, R.id.post_blog_iv_del_iv2, R.id.post_blog_iv_del_iv3,
            R.id.post_blog_iv_del_iv4, R.id.post_blog_ib_add, R.id.post_blog_ib_picture,
            R.id.post_blog_ib_camera, R.id.post_blog_ib_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.post_blog_iv_del_iv1:
                deleteBlogImage(0);
                break;
            case R.id.post_blog_iv_del_iv2:
                deleteBlogImage(1);
                break;
            case R.id.post_blog_iv_del_iv3:
                deleteBlogImage(2);
                break;
            case R.id.post_blog_iv_del_iv4:
                deleteBlogImage(3);
                break;
            case R.id.post_blog_ib_add:
                if (isExpanded) {
                    closeButtons();
                } else {
                    expandButtons();
                }
                break;
            case R.id.post_blog_ib_picture:
                selectSystemPicture();
                break;
            case R.id.post_blog_ib_camera:
                break;
            case R.id.post_blog_ib_location:
                break;
        }
    }

    //打开动画
    private void expandButtons() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_expand_anim);
        postBlogIbPicture.startAnimation(anim);
        postBlogIbCamera.startAnimation(anim);
        postBlogIbLocation.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO: 2017/7/6
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                postBlogIbPicture.setVisibility(View.VISIBLE);
                postBlogIbCamera.setVisibility(View.VISIBLE);
                postBlogIbLocation.setVisibility(View.VISIBLE);
                isExpanded = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO: 2017/7/6
            }
        });
    }

    //关闭动画
    private void closeButtons() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_close_anim);
        postBlogIbPicture.startAnimation(anim);
        postBlogIbCamera.startAnimation(anim);
        postBlogIbLocation.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // TODO: 2017/7/6
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                postBlogIbPicture.setVisibility(View.INVISIBLE);
                postBlogIbCamera.setVisibility(View.INVISIBLE);
                postBlogIbLocation.setVisibility(View.INVISIBLE);
                isExpanded = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO: 2017/7/6
            }
        });
    }

    private void selectSystemPicture() {
        //判断用户是否已经授权，未授权则向用户申请授权，已授权则直接操作
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        } else {
            openSystemPictureWindow();
        }
    }

    private void openSystemPictureWindow() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openSystemPictureWindow();
            } else {
                toast("获取图片权限被拒绝");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (cursor != null) {
                    cursor.moveToNext();
                    String filePath = cursor.getString(0);
                    showBlogImage(filePath);
                    cursor.close();
                }
                //TODO 拍照返回或地图截图返回，只要得到图片的本地路径
                //通过调用showBlogImage就可以将图片放到blogImgs中显示
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //将图片放到blogImages中显示
    private void showBlogImage(String filePath) {
        for (int i = 0; i < blogImages.size(); i++) {
            RelativeLayout layout = blogRLImages.get(i);
            if (layout.getVisibility() == View.INVISIBLE) {
                ImageView iv = blogImages.get(i);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                iv.setImageBitmap(bitmap);
                iv.setTag(filePath);
                layout.setVisibility(View.VISIBLE);
                postBlogTvBlogIvCount.setText(""+(i + 1) + " / 4");
                return;
            }
        }
        toast("最多添加四幅图片");
    }


    private void deleteBlogImage(int position) {
        //1.获取一共显示的图片数量
        int count = 0;
        for (RelativeLayout blogRLImage : blogRLImages) {
            if (blogRLImage.getVisibility() == View.VISIBLE) {
                count +=1;
            }
        }
        //2.如果用户点击的恰好是最后一副配图的“小红叉”
        //  将显示最后一副配图的ImageView和小红叉隐藏
        if (position == count-1){
            blogRLImages.get(position).setVisibility(View.INVISIBLE);
        }else {
            //3.如用用户点击的不是最后一副配图的“小红叉”
            //  需要将用户点击位置后面的配图依次向前递补
            // 直到最后一副配图再将其隐藏
            for (int i = position; i < count; i++) {
                if (i == count-1) {
                    blogRLImages.get(i).setVisibility(View.INVISIBLE);
                }else {
                    Drawable drawable = blogImages.get(i + 1).getDrawable();
                    String filePath = (String) blogImages.get(i + 1).getTag();
                    blogImages.get(i).setImageDrawable(drawable);
                    blogImages.get(i).setTag(filePath);
                }
            }
        }

        //4.配图“删除”后，修改配图数量的显示
        if (count == 1){
            postBlogTvBlogIvCount.setText("");
        }else {
            postBlogTvBlogIvCount.setText(""+(count-1)+" / 4");
        }
    }
}
