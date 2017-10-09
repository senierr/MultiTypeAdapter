package com.senierr.rvadapter.link;

import android.support.annotation.NonNull;

import com.senierr.rvadapter.ViewHolderWrapper;

/**
 * 一对一关系
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class OneToOneLink<T> extends DefaultLink<T> {

    @Override
    public final Class<? extends ViewHolderWrapper<T>> onAssignedWithType(@NonNull T item) {
        return null;
    }

    @Override
    public int onAssignedWithIndex(@NonNull T item) {
        return 0;
    }
}
