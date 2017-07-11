package com.kingsley.miaoxin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingsley.miaoxin.R;
import com.kingsley.miaoxin.entity.Comment;

import java.util.List;

import butterknife.BindView;

/**
 * class name : MiaoXin
 * author : Kingsley
 * created date : on 2017/7/10 10:23
 * file change date : on 2017/7/10 10:23
 * version: 1.0
 */

public class CommentAdapter extends MyBaseAdapter<Comment> {

    public CommentAdapter(Context context, List<Comment> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        CommentViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.bolg_item_comment_layout, viewGroup, false);
            holder = new CommentViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (CommentViewHolder) convertView.getTag();
        }
        Comment comment = datas.get(position);
        holder.mTvCommentUsername.setText(comment.getUsername());
        holder.mTvCommentContent.setText(comment.getContent());
        Log.i("TAG", "getView: getCount="+getCount());
        setListener(holder,comment);
        return convertView;
    }

    private void setListener(CommentViewHolder holder, Comment comment) {
        holder.mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    class CommentViewHolder extends ViewHolder {
        @BindView(R.id.blog_comment_item_username)
        TextView mTvCommentUsername;
        @BindView(R.id.blog_comment_item_content)
        TextView mTvCommentContent;
        @BindView(R.id.blog_comment_item_ll_comment_container)
        LinearLayout mLlCommentContainer;
        @BindView(R.id.blog_comment_item_et_comment)
        EditText mEtComment;
        @BindView(R.id.blog_comment_item__btn_send)
        Button mBtnSend;

        CommentViewHolder(View view) {
            super(view);
        }
    }
}
