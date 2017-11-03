package com.senierr.demo;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.senierr.adapter.core.ViewHolderWrapper;
import com.senierr.adapter.core.RVHolder;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class SecondWrapper extends ViewHolderWrapper<DataBean> {

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        final RVHolder rvHolder = RVHolder.create(parent, R.layout.item_second);
        CheckBox checkBox = rvHolder.getView(R.id.cb_check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(compoundButton.getContext(), rvHolder.getLayoutPosition() + ", Check: " + b, Toast.LENGTH_SHORT).show();
            }
        });
        return rvHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull DataBean item) {
        TextView textView = holder.getView(R.id.tv_text);
        textView.setText(item.getContent());
        textView.setHeight(item.getHeight());
        CheckBox checkBox = holder.getView(R.id.cb_check);
        checkBox.setChecked(false);
    }
}