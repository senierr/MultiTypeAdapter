package com.senierr.demo;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.senierr.adapter.ViewHolderWrapper;
import com.senierr.adapter.util.RVHolder;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class FirstWrapper extends ViewHolderWrapper<DataBean> {

    public FirstWrapper() {
        super(DataBean.class, R.layout.item_first);
    }

    @Override
    public boolean onAcceptAssignment(DataBean item) {
        if (item.getId()!= 0 && item.getId() % 2 == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull DataBean item) {
        TextView textView = holder.getView(R.id.tv_text);
        textView.setText(item.getContent());
    }
}