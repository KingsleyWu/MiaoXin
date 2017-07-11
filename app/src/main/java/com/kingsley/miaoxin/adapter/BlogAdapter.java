package com.kingsley.miaoxin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kingsley.miaoxin.R;
import com.kingsley.miaoxin.app.MyApp;
import com.kingsley.miaoxin.entity.Blog;
import com.kingsley.miaoxin.entity.Comment;
import com.kingsley.miaoxin.util.TimeUtil;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * class name : MiaoXin
 * author : Kingsley
 * created date : on 2017/7/7 20:23
 * file change date : on 2017/7/7 20:23
 * version: 1.0
 */

public class BlogAdapter extends MyBaseAdapter<Blog> {

    public BlogAdapter(Context context, List<Blog> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BlogViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.find_blog_item_layout, parent, false);
            holder = new BlogViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BlogViewHolder) convertView.getTag();
        }
        final Blog blog = datas.get(position);
        setAvatar(blog.getAuthor().getAvatar(), holder.blogAvatar);
        holder.blogContent.setText(blog.getContent());
        holder.blogUsername.setText(blog.getAuthor().getUsername());
        holder.blogLove.setText(""+blog.getLoveUsers().size()+"赞");
        holder.blogTime.setText(blog.getUpdatedAt());
        BmobQuery<Comment> query = new BmobQuery<>();
        final List<Comment> comments = new ArrayList<>();
        final CommentAdapter adapter = new CommentAdapter(context, comments);
        holder.lvBlogCommentContainer.setAdapter(adapter);
        query.addWhereEqualTo("blogId",blog.getObjectId());
        query.findObjects(context, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {

                //adapter.addAll(list,true);
                Log.d("TAG", "onSuccess: i="+list.size() +"blog.getObjectId()"+blog.getObjectId());
            }
            @Override
            public void onError(int i, String s) {
                Log.d("TAG", "onError: i="+i+"  s="+s);
            }
        });
        setListener(holder,parent,blog);
        //配图的呈现
        holder.blogImageContainer.removeAllViews();
        String imgUrls = blog.getImgUrls();
        if(!TextUtils.isEmpty(imgUrls)){
            showBlogImages(imgUrls,holder.blogImageContainer);
        }
        //yyyy-MM-dd HH:mm:ss

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(blog.getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.blogTime.setText(TimeUtil.getTime(date != null ? date.getTime() : 0));
        return convertView;
    }

    private void showBlogImages(String imgUrls, RelativeLayout blogImageContainer) {
        //图1&图2&图3&图4
        String[] urls = imgUrls.split("&");

        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());

        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        //左外边距(10dp)+右外边距(10dp)+margin(15dp)
        int span = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());

        int size = (screenWidth-span)/4;

        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());

        for(int i=0;i<urls.length;i++){
            final ImageView iv = new ImageView(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
            params.leftMargin = i%4*(size+margin);
            params.topMargin = i/4*(size+margin);

            iv.setLayoutParams(params);
            //显示图片
            Picasso.with(context).load(urls[i]).into(iv);
            iv.setBackgroundResource(R.drawable.input_bg);
            iv.setPadding(padding, padding, padding, padding);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            blogImageContainer.addView(iv);
        }
    }

    private void setListener(final BlogViewHolder holder, ViewGroup parent, final Blog blog) {
        holder.blogLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = BmobUser.getCurrentUser(MyApp.context).getUsername();
                if (blog.getLoveUsers().contains(username)) {
                    blog.getLoveUsers().remove(username);
                }else {
                    blog.getLoveUsers().add(username);
                }
                blog.update(MyApp.context, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        holder.blogLove.setText(""+blog.getLoveUsers().size()+"赞");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.d("TAG", "onFailure: i="+i+"  s="+s);
                    }
                });
            }
        });

        holder.blogComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.llCommentContainer.setVisibility(View.VISIBLE);
            }
        });

        holder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.blogEtComment.getText() != null) {
                    holder.llCommentContainer.setVisibility(View.GONE);
                    Comment comment = new Comment();
                    comment.setBlogId(blog.getObjectId());
                    comment.setContent(holder.blogEtComment.getText().toString());
                    comment.setUsername(BmobUser.getCurrentUser(context).getUsername());
                    comment.save(context, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            holder.blogEtComment.setText("");
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Log.d("TAG", "onFailure: i="+i+"  s="+s);
                        }
                    });
                }else {
                    Toast.makeText(context, "亲! 请输入喵评...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        parent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        holder.llCommentContainer.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });
    }

    class BlogViewHolder extends ViewHolder {
        @BindView(R.id.find_item_iv_blog_avatar)
        ImageView blogAvatar;
        @BindView(R.id.find_item_tv_blog_username)
        TextView blogUsername;
        @BindView(R.id.find_item_tv_blog_content)
        TextView blogContent;
        @BindView(R.id.find_item_rl_blog_image_container)
        RelativeLayout blogImageContainer;
        @BindView(R.id.find_item_tv_blog_time)
        TextView blogTime;
        @BindView(R.id.find_item_tv_blog_love)
        TextView blogLove;
        @BindView(R.id.find_item_tv_blog_comment)
        TextView blogComment;
        @BindView(R.id.find_item_et_comment)
        EditText blogEtComment;
        @BindView(R.id.find_item_btn_send)
        Button btnSend;
        @BindView(R.id.find_item_ll_comment_container)
        LinearLayout llCommentContainer;
        @BindView(R.id.find_item_lv_blog_comment)
        ListView lvBlogCommentContainer;

        BlogViewHolder(View view) {
            super(view);
        }
    }

}
