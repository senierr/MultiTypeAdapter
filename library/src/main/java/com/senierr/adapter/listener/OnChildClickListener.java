package com.senierr.adapter.listener;

import android.view.View;

import com.senierr.adapter.internal.ViewHolder;

/**
 * 列表子项点击事件
 *
 * @author zhouchunjie
 * @date 2018/11/2
 */
public interface OnChildClickListener<T> {

    void onClick(ViewHolder viewHolder, View view, int position, T t);
}
