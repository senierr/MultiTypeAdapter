package com.senierr.demo;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.senierr.rvadapter.ViewHolderWrapper;
import com.senierr.rvadapter.util.RVHolder;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class FirstWrapper extends ViewHolderWrapper<DataBean> {

    @Override
    public int getLayoutId() {
        return R.layout.item_first;
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull DataBean item) {
        TextView textView = holder.getView(R.id.tv_text);
        textView.setText(item.getContent());
    }
}