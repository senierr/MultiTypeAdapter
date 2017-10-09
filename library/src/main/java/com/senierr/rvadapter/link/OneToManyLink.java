package com.senierr.rvadapter.link;

import android.support.annotation.NonNull;

import com.senierr.rvadapter.ViewHolderWrapper;
import com.senierr.rvadapter.exception.WrapperNotFoundException;

/**
 * 一对多关系
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */

public abstract class OneToManyLink<T> extends DefaultLink<T> {

    @Override
    public final int convertToIndex(@NonNull T item) {
        ViewHolderWrapper<?>[] viewHolderWrappers = getViewHolderWrappers();
        Class<? extends ViewHolderWrapper<T>> wrapperCls = onAssigned(item);
        for (int i = 0; i < viewHolderWrappers.length; i++) {
            if (viewHolderWrappers[i].getClass().equals(wrapperCls)) {
                return i;
            }
        }
        throw new WrapperNotFoundException(item.getClass());
    }
}
