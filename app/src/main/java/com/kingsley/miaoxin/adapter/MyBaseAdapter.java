package com.kingsley.miaoxin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kingsley.miaoxin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;

/**
 * class name : MiaoXin
 * author : Kingsley
 * created date : on 2017/7/7 20:25
 * file change date : on 2017/7/7 20:25
 * version: 1.0
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    protected Context context;
    LayoutInflater inflater;
    List<T> datas;

    public MyBaseAdapter(Context context, List<T> datas) {
        this.context = context;
        this.datas = datas;
        //inflater = LayoutInflater.from(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(T t){
        datas.add(t);
        notifyDataSetChanged();
    }

    public void addAll(List<T> list,boolean isClear){
        if (isClear){
            datas.clear();
        }
        datas.addAll(list);
        notifyDataSetChanged();
    }

    public void remove(T t){
        datas.remove(t);
        notifyDataSetChanged();
    }

    public void clear(){
        datas.clear();
        notifyDataSetChanged();
    }
    public void setAvatar(String url, ImageView iv){
        if(TextUtils.isEmpty(url)){
            iv.setImageResource(R.drawable.ic_launcher);
        }else{
            Picasso.with(context).load(url).into(iv);
        }
    }

    public class ViewHolder{
        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
