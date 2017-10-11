package com.senierr.demo;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.senierr.adapter.ViewHolderWrapper;
import com.senierr.adapter.util.RVHolder;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class SecondWrapper extends ViewHolderWrapper<DataBean> {

    public SecondWrapper() {
        super(DataBean.class, R.layout.item_second);
    }

    @Override
    public boolean onAcceptAssignment(DataBean item) {
        if (item.getId()!= 0 && item.getId() % 2 != 0) {
            return true;
        }
        return false;
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