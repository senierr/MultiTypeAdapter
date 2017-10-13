package com.senierr.demo;

import android.support.annotation.NonNull;

import com.senierr.adapter.support.BaseStateWrapper;
import com.senierr.adapter.util.RVHolder;

/**
 * @author zhouchunjie
 * @date 2017/10/13
 */

public class StateWrapper extends BaseStateWrapper {

    public StateWrapper() {
        super(R.layout.item_state);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull StateBean item) {
        switch (item.getState()) {
            case StateBean.STATE_LOADING:
                holder.setText(R.id.tv_text, R.string.loading);
                holder.setGone(R.id.pb_bar, false);
                break;
            case StateBean.STATE_EMPTY:
                holder.setText(R.id.tv_text, R.string.empty);
                holder.setGone(R.id.pb_bar, true);
                break;
            case StateBean.STATE_ERROR:
                holder.setText(R.id.tv_text, R.string.error);
                holder.setGone(R.id.pb_bar, true);
                break;
            case StateBean.STATE_NO_NETWORK:
                holder.setText(R.id.tv_text, R.string.no_network);
                holder.setGone(R.id.pb_bar, true);
                break;
        }
    }
}
