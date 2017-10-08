package com.senierr.adapterlib.binder;

import android.support.annotation.NonNull;

import com.senierr.adapterlib.ViewHolderWrapper;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public abstract class Binder<T> {

    protected @NonNull ViewHolderWrapper<?>[] wrappers;

    public abstract Class<? extends ViewHolderWrapper<T>> onGetWrapperType(@NonNull T item);

    public abstract int index(@NonNull T item);

    @NonNull
    public ViewHolderWrapper<?>[] getWrappers() {
        return wrappers;
    }

    public void setWrappers(@NonNull ViewHolderWrapper<?>[] wrappers) {
        this.wrappers = wrappers;
    }
}
