package com.senierr.adapter.listener;

import android.support.annotation.NonNull;
import android.view.View;

import com.senierr.adapter.internal.ViewHolder;

/**
 * 列表子项长按事件
 *
 * @author zhouchunjie
 * @date 2018/11/2
 */
public interface OnChildLongClickListener<T> {

    boolean onLongClick(@NonNull ViewHolder viewHolder, @NonNull View view, int position, @NonNull T t);
}
