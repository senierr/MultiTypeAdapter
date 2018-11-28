package com.senierr.adapter.internal;

import android.support.annotation.NonNull;

/**
 * 数据-视图连接器
 *
 * 用于描述数据类型所对应的视图类型
 *
 * @author zhouchunjie
 * @date 2018/11/5
 */
public interface Linker<T> {
    @NonNull
    ViewHolderWrapper<T> getItemViewType(@NonNull T item);
}