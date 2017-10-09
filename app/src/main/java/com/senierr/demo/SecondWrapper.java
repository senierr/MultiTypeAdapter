package com.senierr.demo;

import android.support.annotation.NonNull;
import android.view.View;
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

    @Override
    public void onViewHolderCreate(@NonNull final RVHolder holder) {
        CheckBox checkBox = holder.getView(R.id.cb_check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(compoundButton.getContext(), holder.getLayoutPosition() + ", Check: " + b, Toast.LENGTH_SHORT).show();
            }
        });
    }
}