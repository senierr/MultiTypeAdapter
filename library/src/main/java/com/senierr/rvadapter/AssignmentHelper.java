package com.senierr.rvadapter;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.senierr.rvadapter.link.OneToManyLink;
import com.senierr.rvadapter.link.OneToOneLink;
import com.senierr.rvadapter.exception.WrapperNotFoundException;

/**
 * 分配帮助类
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class AssignmentHelper<T> {

    private LinkedViewTypeMap linkedViewTypeMap;
    private Class<T> cls;
    private ViewHolderWrapper<T>[] viewHolderWrappers;
    private boolean isAssigned;

    AssignmentHelper(LinkedViewTypeMap linkedViewTypeMap) {
        this.linkedViewTypeMap = linkedViewTypeMap;
        isAssigned = false;
    }

    @NonNull @CheckResult
    final AssignmentHelper<T> assign(@NonNull Class<T> cls) {
        this.cls = cls;
        return this;
    }

    @NonNull
    public final AssignmentHelper<T> to(@NonNull ViewHolderWrapper<T> viewHolderWrapper) {
        linkedViewTypeMap.put(cls, viewHolderWrapper, new OneToOneLink<T>());
        isAssigned = true;
        return this;
    }

    @NonNull @CheckResult @SafeVarargs
    public final AssignmentHelper<T> to(@NonNull ViewHolderWrapper<T>... viewHolderWrappers) {
        if (viewHolderWrappers.length == 0) {
            throw new WrapperNotFoundException(cls);
        } else {
            this.viewHolderWrappers = viewHolderWrappers;
        }
        return this;
    }

    @NonNull
    public final AssignmentHelper<T> by(@NonNull OneToManyLink<T> oneToManyLink) {
        oneToManyLink.setViewHolderWrappers(viewHolderWrappers);
        for (ViewHolderWrapper<T> wrapper : viewHolderWrappers) {
            linkedViewTypeMap.put(cls, wrapper, oneToManyLink);
        }
        isAssigned = true;
        return this;
    }

    public boolean isAssigned() {
        return isAssigned;
    }
}
