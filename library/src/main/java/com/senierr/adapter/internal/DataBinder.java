package com.senierr.adapter.internal;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * 绑定器
 *
 * @author zhouchunjie
 * @date 2017/10/18
 */

public abstract class DataBinder<T> implements Cloneable {

    private Class<T> dataCls;
    private ViewHolderWrapper<T> viewHolderWrapper;

    /**
     * 数据对应的封装器的索引
     *
     * @param item
     * @return
     */
    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public abstract int onBindIndex(@NonNull T item);

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NonNull
    Class<T> getDataCls() {
        return dataCls;
    }

    void setDataCls(@NonNull Class<T> dataCls) {
        this.dataCls = dataCls;
    }

    void setViewHolderWrapper(@NonNull ViewHolderWrapper<T> viewHolderWrapper) {
        this.viewHolderWrapper = viewHolderWrapper;
    }

    @NonNull
    ViewHolderWrapper<T> getViewHolderWrapper() {
        return viewHolderWrapper;
    }
}