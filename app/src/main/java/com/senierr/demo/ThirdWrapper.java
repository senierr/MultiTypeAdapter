package com.senierr.demo;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.senierr.adapter.ViewHolderWrapper;
import com.senierr.adapter.util.RVHolder;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class ThirdWrapper extends ViewHolderWrapper<DataBean> {

    public ThirdWrapper() {
        super(DataBean.class, R.layout.item_third);
    }

    @Override
    public boolean onAcceptAssignment(DataBean item) {
        if (item.getId() == 0) {
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