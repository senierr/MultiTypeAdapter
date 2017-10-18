package com.senierr.adapter.core;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

/**
 * @author zhouchunjie
 * @date 2017/10/18
 */

public abstract class DataBinder<T> {

    private ViewHolderWrapper<T>[] viewHolderWrappers;

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public abstract int onBindIndex(@NonNull T item);

    void setViewHolderWrappers(@NonNull ViewHolderWrapper<T>[] viewHolderWrappers) {
        this.viewHolderWrappers = viewHolderWrappers;
    }

    @NonNull
    ViewHolderWrapper<T> getViewHolderWrapper(@NonNull T item) {
        return viewHolderWrappers[onBindIndex(item)];
    }
}