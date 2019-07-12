package com.senierr.demo.wrapper;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.senierr.adapter.internal.ViewHolder;
import com.senierr.adapter.support.bean.StateBean;
import com.senierr.adapter.support.wrapper.BaseStateWrapper;
import com.senierr.demo.R;

/**
 * @author zhouchunjie
 * @date 2017/10/13
 */
public class StateWrapper extends BaseStateWrapper {

    public final static int STATE_DEFAULT = 0;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return ViewHolder.Companion.create(parent, R.layout.item_state);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull StateBean item) {
        TextView textView = holder.findView(R.id.tv_text);
        ProgressBar progressBar = holder.findView(R.id.pb_bar);
        switch (item.getState()) {
            case STATE_DEFAULT:
                textView.setText(R.string.state_default);
                progressBar.setVisibility(View.GONE);
                break;
        }
    }
}
