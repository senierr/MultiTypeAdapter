package com.senierr.demo;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.senierr.rvadapter.ViewHolderWrapper;
import com.senierr.rvadapter.util.RVHolder;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class SecondWrapper extends ViewHolderWrapper<DataBean> {

    @Override
    public int getLayoutId() {
        return R.layout.item_second;
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull DataBean item) {
        TextView textView = holder.getView(R.id.tv_text);
        textView.setText(item.getContent());
    }

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        final RVHolder rvHolder = super.onCreateViewHolder(parent);
        CheckBox checkBox = rvHolder.getView(R.id.cb_check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(compoundButton.getContext(), rvHolder.getLayoutPosition() + ", Check: " + b, Toast.LENGTH_SHORT).show();
            }
        });
        return rvHolder;
    }
}