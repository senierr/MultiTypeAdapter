package com.senierr.demo.wrapper;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.senierr.adapter.internal.ViewHolderWrapper;
import com.senierr.adapter.internal.ViewHolder;
import com.senierr.demo.DataBean;
import com.senierr.demo.R;

import org.jetbrains.annotations.NotNull;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */
public class FirstWrapper extends ViewHolderWrapper<DataBean> {

    public FirstWrapper() {
        super(DataBean.class, R.layout.item_first);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull DataBean item) {
        TextView textView = holder.findView(R.id.tv_text);
        textView.setText(item.getContent());
        textView.setHeight(item.getHeight());
    }
}