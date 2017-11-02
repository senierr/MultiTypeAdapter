package com.senierr.adapter.core;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * @author zhouchunjie
 * @date 2017/10/18
 */

public abstract class DataBinder<T> {

    private Class<T> dataCls;
    private ViewHolderWrapper<T>[] viewHolderWrappers;

    /**
     * 数据对应的封装器的索引
     *
     * @param item
     * @return
     */
    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public abstract int onBindIndex(@NonNull T item);

    @NonNull
    Class<T> getDataCls() {
        return dataCls;
    }

    void setDataCls(@NonNull Class<T> dataCls) {
        this.dataCls = dataCls;
    }

    void setViewHolderWrappers(@NonNull ViewHolderWrapper<T>[] viewHolderWrappers) {
        this.viewHolderWrappers = viewHolderWrappers;
    }

    @NonNull
    ViewHolderWrapper<T>[] getViewHolderWrappers() {
        return viewHolderWrappers;
    }
}