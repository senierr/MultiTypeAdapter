package com.senierr.demo;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.senierr.adapter.support.wrapper.BaseLoadMoreWrapper;
import com.senierr.adapter.internal.RVHolder;
import com.senierr.adapter.support.bean.LoadMoreBean;

/**
 * @author zhouchunjie
 * @date 2017/10/11
 */

public class LoadMoreWrapper extends BaseLoadMoreWrapper {

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return RVHolder.create(parent, R.layout.item_load_more);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull LoadMoreBean item) {
        switch (item.getLoadState()) {
            case LoadMoreBean.STATUS_LOADING:
                holder.setText(R.id.tv_text, R.string.loading);
                holder.setGone(R.id.pb_bar, false);
                break;
            case LoadMoreBean.STATUS_LOADING_COMPLETED:
                holder.setText(R.id.tv_text, R.string.completed);
                holder.setGone(R.id.pb_bar, true);
                break;
            case LoadMoreBean.STATUS_LOAD_NO_MORE:
                holder.setText(R.id.tv_text, R.string.no_more);
                holder.setGone(R.id.pb_bar, true);
                break;
            case LoadMoreBean.STATUS_LOAD_FAILURE:
                holder.setText(R.id.tv_text, R.string.failure);
                holder.setGone(R.id.pb_bar, true);
                break;
        }
    }
}
