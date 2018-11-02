package com.senierr.adapter.listener;

import com.senierr.adapter.internal.ViewHolder;

/**
 * 列表项点击事件
 *
 * @author zhouchunjie
 * @date 2018/11/2
 */
public interface OnItemClickListener<T> {

    void onClick(ViewHolder viewHolder, int position, T t);
}
