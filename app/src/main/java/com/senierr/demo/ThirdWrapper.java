package com.senierr.demo;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
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