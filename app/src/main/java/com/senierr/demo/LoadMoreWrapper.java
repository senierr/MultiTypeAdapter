package com.senierr.demo;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.senierr.adapter.support.BaseLoadMoreWrapper;
import com.senierr.adapter.util.RVHolder;

/**
 * @author zhouchunjie
 * @date 2017/10/11
 */

public class LoadMoreWrapper extends BaseLoadMoreWrapper {

    public LoadMoreWrapper() {
        super(R.layout.item_third);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull LoadMoreBean item) {
        TextView textView = holder.getView(R.id.tv_text);
        switch (item.getLoadState()) {
            case LoadMoreBean.STATUS_LOADING:
                textView.setText("正在加载...");
                break;
            case LoadMoreBean.STATUS_LOADING_COMPLETED:
                textView.setText("加载完成");
                break;
            case LoadMoreBean.STATUS_LOAD_NO_MORE:
                textView.setText("没有更多");
                break;
            case LoadMoreBean.STATUS_LOAD_FAILURE:
                textView.setText("加载失败");
                break;
        }
    }
}
