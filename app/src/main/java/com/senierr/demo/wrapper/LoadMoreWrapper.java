package com.senierr.demo.wrapper;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.senierr.adapter.support.wrapper.BaseLoadMoreWrapper;
import com.senierr.adapter.internal.ViewHolder;
import com.senierr.adapter.support.bean.LoadMoreBean;
import com.senierr.demo.R;

/**
 * @author zhouchunjie
 * @date 2017/10/11
 */
public class LoadMoreWrapper extends BaseLoadMoreWrapper {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return ViewHolder.create(parent, R.layout.item_load_more);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull LoadMoreBean item) {
        TextView textView = holder.findView(R.id.tv_text);
        ProgressBar progressBar = holder.findView(R.id.pb_bar);
        switch (item.getLoadState()) {
            case LoadMoreBean.STATUS_LOADING:
                textView.setText(R.string.loading);
                progressBar.setVisibility(View.VISIBLE);
                break;
            case LoadMoreBean.STATUS_COMPLETED:
                textView.setText(R.string.completed);
                progressBar.setVisibility(View.GONE);
                break;
            case LoadMoreBean.STATUS_NO_MORE:
                textView.setText(R.string.no_more);
                progressBar.setVisibility(View.GONE);
                break;
            case LoadMoreBean.STATUS_FAILURE:
                textView.setText(R.string.failure);
                progressBar.setVisibility(View.GONE);
                break;
        }
    }
}
