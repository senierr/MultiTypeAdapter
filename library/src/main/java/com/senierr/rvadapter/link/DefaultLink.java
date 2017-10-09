package com.senierr.rvadapter.link;

import android.support.annotation.NonNull;

import com.senierr.rvadapter.ViewHolderWrapper;

/**
 * 数据类型与工人对应的关系
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */

public abstract class DefaultLink<T> {

    private ViewHolderWrapper<?>[] viewHolderWrappers;

    public abstract Class<? extends ViewHolderWrapper<T>> onAssignedWithType(@NonNull T item);

    public int onAssignedWithIndex(@NonNull T item) {
        return 0;
    }

    @NonNull
    public final ViewHolderWrapper<?>[] getViewHolderWrappers() {
        return viewHolderWrappers;
    }

    public final void setViewHolderWrappers(@NonNull ViewHolderWrapper<?>[] viewHolderWrappers) {
        this.viewHolderWrappers = viewHolderWrappers;
    }
}
