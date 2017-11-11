package com.senierr.demo;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.senierr.adapter.internal.ViewHolderWrapper;
import com.senierr.adapter.internal.RVHolder;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class FirstWrapper extends ViewHolderWrapper<DataBean> {

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return RVHolder.create(parent, R.layout.item_first);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull DataBean item) {
        TextView textView = holder.getView(R.id.tv_text);
        textView.setText(item.getContent());
        textView.setHeight(item.getHeight());
    }


}