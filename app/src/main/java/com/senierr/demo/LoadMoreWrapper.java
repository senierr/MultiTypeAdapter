package com.senierr.demo;

import android.support.annotation.NonNull;

import com.senierr.adapter.support.BaseLoadMoreWrapper;
import com.senierr.adapter.util.RVHolder;

/**
 * @author zhouchunjie
 * @date 2017/10/11
 */

public class LoadMoreWrapper extends BaseLoadMoreWrapper {

    public LoadMoreWrapper() {
        super(R.layout.item_load_more);
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
