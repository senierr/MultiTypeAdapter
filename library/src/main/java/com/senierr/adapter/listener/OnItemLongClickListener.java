package com.senierr.adapter.listener;

import android.support.annotation.NonNull;

import com.senierr.adapter.internal.ViewHolder;

/**
 * 列表项长按事件
 *
 * @author zhouchunjie
 * @date 2018/11/2
 */
public interface OnItemLongClickListener<T> {

    boolean onLongClick(@NonNull ViewHolder viewHolder, int position, @NonNull T t);
}
