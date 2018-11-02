package com.senierr.adapter.listener;

import com.senierr.adapter.internal.ViewHolder;

/**
 * 列表项长按事件
 *
 * @author zhouchunjie
 * @date 2018/11/2
 */
public interface OnItemLongClickListener<T> {

    boolean onLongClick(ViewHolder viewHolder, int position, T t);
}
