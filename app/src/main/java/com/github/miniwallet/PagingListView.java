package com.github.miniwallet;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.github.miniwallet.adapters.EntityListAdapter;

import java.util.List;

/**
 * Created by Agnieszka on 2015-05-29.
 */
public class PagingListView<T> extends ListView implements AbsListView.OnScrollListener {

    private boolean isLoading;
    private PagingListener listener;
    private EntityListAdapter<T> adapter;

    public static interface PagingListener {
        public void loadData();
    }


    public PagingListView(Context context) {
        super(context);
        this.setOnScrollListener(this);
    }

    public PagingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnScrollListener(this);
    }

    public PagingListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnScrollListener(this);
    }

    public void setAdapter(EntityListAdapter<T> adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
    }

    public void addNewData(List<T> data) {
        Log.i("PagingListView", " Adapter= null is " + (adapter == null));
        adapter.getCount();
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
        isLoading = false;
    }

    public void setListener(PagingListener listener) {
        this.listener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (getAdapter() == null)
            return;
        if (getAdapter().getCount() == 0)
            return;
        if (visibleItemCount + firstVisibleItem >= totalItemCount && !isLoading) {
            isLoading = true;
            listener.loadData();
        }
    }
}
