package com.senierr.adapter.listener;

import android.view.View;

import com.senierr.adapter.internal.ViewHolder;

/**
 * 列表子项长按事件
 *
 * @author zhouchunjie
 * @date 2018/11/2
 */
public interface OnChildLongClickListener<T> {

    boolean onLongClick(ViewHolder viewHolder, View view, int position, T t);
}
