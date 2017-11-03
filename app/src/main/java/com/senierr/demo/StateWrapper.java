package com.senierr.demo;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.senierr.seadapter.support.wrapper.BaseStateWrapper;
import com.senierr.seadapter.internal.RVHolder;
import com.senierr.seadapter.support.bean.StateBean;

/**
 * @author zhouchunjie
 * @date 2017/10/13
 */

public class StateWrapper extends BaseStateWrapper {

    public final static int STATE_DEFAULT = 0;

    @NonNull
    @Override
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return RVHolder.create(parent, R.layout.item_state);
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
            case STATE_DEFAULT:
                holder.setText(R.id.tv_text, R.string.state_default);
                holder.setGone(R.id.pb_bar, true);
                break;
        }
    }
}
