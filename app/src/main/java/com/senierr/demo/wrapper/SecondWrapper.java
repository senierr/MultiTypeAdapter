package com.senierr.demo.wrapper;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.senierr.adapter.internal.ViewHolderWrapper;
import com.senierr.adapter.internal.ViewHolder;
import com.senierr.demo.DataBean;
import com.senierr.demo.R;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */
public class SecondWrapper extends ViewHolderWrapper<DataBean> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        final ViewHolder rvHolder = ViewHolder.Companion.create(parent, R.layout.item_second);
        CheckBox checkBox = rvHolder.findView(R.id.cb_check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(compoundButton.getContext(), rvHolder.getLayoutPosition() + ", Check: " + b, Toast.LENGTH_SHORT).show();
            }
        });
        return rvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull DataBean item) {
        TextView textView = holder.findView(R.id.tv_text);
        textView.setText(item.getContent());
        textView.setHeight(item.getHeight());
        CheckBox checkBox = holder.findView(R.id.cb_check);
        checkBox.setChecked(false);
    }
}