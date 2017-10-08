package com.senierr.adapterlib.link;

import android.support.annotation.NonNull;

import com.senierr.adapterlib.ViewHolderWrapper;

/**
 * 一对一关系
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class OneToOneLink<T> extends DefaultLink<T> {

    @Override
    public final Class<? extends ViewHolderWrapper<T>> onAssigned(@NonNull T item) {
        return null;
    }

    @Override
    public final int convertToIndex(@NonNull T item) {
        return 0;
    }
}
