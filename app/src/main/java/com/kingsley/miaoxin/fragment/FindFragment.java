package com.kingsley.miaoxin.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kingsley.miaoxin.R;
import com.kingsley.miaoxin.adapter.BlogAdapter;
import com.kingsley.miaoxin.entity.Blog;
import com.kingsley.miaoxin.entity.MyUser;
import com.kingsley.miaoxin.ui.PostBlogActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends BaseFragment {
    ListView listView;
    List<Blog> blogs;
    BlogAdapter adapter;

    @BindView(R.id.find_frg_ptr_blog_list)
    PullToRefreshListView ptrListView;

    public FindFragment() {
        // Required empty public constructor
    }

    @Override
    public View createMyView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find, container, false);
    }

    @Override
    public void doBusiness() {
        MyUser currentUser = BmobUser.getCurrentUser(baseActivity, MyUser.class);
        log("username ="+currentUser.getGender());
        listView = ptrListView.getRefreshableView();
        blogs = new ArrayList<>();
        adapter = new BlogAdapter(baseActivity, blogs);
        listView.setAdapter(adapter);

        ptrListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        BmobQuery<Blog> query = new BmobQuery<>();
        query.include("author");
        query.order("-createdAt");
        query.findObjects(getActivity(), new FindListener<Blog>() {
            @Override
            public void onSuccess(List<Blog> arg0) {
                adapter.addAll(arg0, true);
                ptrListView.onRefreshComplete();
            }

            @Override
            public void onError(int arg0, String arg1) {
                baseActivity.toastAndLog("刷新帖子失败，请稍后重试", arg0, arg1);
                ptrListView.onRefreshComplete();

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_menu_new_blog, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        baseActivity.jumpTo(PostBlogActivity.class, false, true);
        return super.onOptionsItemSelected(item);
    }
}
