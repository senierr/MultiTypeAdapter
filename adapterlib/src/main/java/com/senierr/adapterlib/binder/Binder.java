package com.senierr.adapterlib.binder;

import android.support.annotation.NonNull;

import com.senierr.adapterlib.ViewWrapper;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public abstract class Binder<T> {

    protected @NonNull ViewWrapper<?>[] wrappers;

    public abstract Class<? extends ViewWrapper<T>> onGetWrapperType(@NonNull T item);

    public abstract int index(@NonNull T item);

    @NonNull
    public ViewWrapper<?>[] getWrappers() {
        return wrappers;
    }

    public void setWrappers(@NonNull ViewWrapper<?>[] wrappers) {
        this.wrappers = wrappers;
    }
}
