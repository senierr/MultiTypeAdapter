package com.senierr.adapterlib.binder;

import android.support.annotation.NonNull;

import com.senierr.adapterlib.ViewWrapper;
import com.senierr.adapterlib.exception.WrapperNotFoundException;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public abstract class OneToManyBinder<T> extends Binder<T> {

    @Override
    public final int index(@NonNull T item) {
        Class<? extends ViewWrapper<T>> wrapperCls = onGetWrapperType(item);
        for (int i = 0; i < wrappers.length; i++) {
            if (wrappers[i].getClass().equals(wrapperCls)) {
                return i;
            }
        }
        throw new WrapperNotFoundException(item.getClass());
    }
}
