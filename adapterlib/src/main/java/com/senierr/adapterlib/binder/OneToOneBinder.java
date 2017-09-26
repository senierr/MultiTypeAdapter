package com.senierr.adapterlib.binder;

import android.support.annotation.NonNull;

import com.senierr.adapterlib.ViewWrapper;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class OneToOneBinder<T> extends Binder<T> {

    @Override
    public final Class<? extends ViewWrapper<T>> onGetWrapperType(@NonNull T item) {
        return null;
    }

    @Override
    public final int index(@NonNull T item) {
        return 0;
    }
}
