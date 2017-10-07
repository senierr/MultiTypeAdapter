package com.senierr.adapterlib;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import com.senierr.adapterlib.binder.OneToManyBinder;
import com.senierr.adapterlib.binder.OneToOneBinder;
import com.senierr.adapterlib.exception.WrapperNotFoundException;

/**
 * 注册帮助类
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class RegisterHelper<T> {

    private LinkedViewTypeMap linkedViewTypeMap;
    private Class<T> cls;
    private ViewWrapper<T>[] wrappers;
    private boolean isRegistered;

    RegisterHelper(LinkedViewTypeMap linkedViewTypeMap) {
        this.linkedViewTypeMap = linkedViewTypeMap;
        isRegistered = false;
    }

    @NonNull @CheckResult
    final RegisterHelper<T> register(@NonNull Class<T> cls) {
        this.cls = cls;
        return this;
    }

    @NonNull
    public final RegisterHelper<T> with(@NonNull ViewWrapper<T> wrapper) {
        linkedViewTypeMap.put(cls, wrapper, new OneToOneBinder<T>());
        isRegistered = true;
        return this;
    }

    @NonNull @CheckResult @SafeVarargs
    public final RegisterHelper<T> with(@NonNull ViewWrapper<T>... wrappers) {
        if (wrappers.length == 0) {
            throw new WrapperNotFoundException(cls);
        } else {
            this.wrappers = wrappers;
        }
        return this;
    }

    @NonNull
    public final RegisterHelper<T> by(@NonNull OneToManyBinder<T> binder) {
        binder.setWrappers(wrappers);
        for (ViewWrapper<T> wrapper : wrappers) {
            linkedViewTypeMap.put(cls, wrapper, binder);
        }
        isRegistered = true;
        return this;
    }

    public boolean isRegistered() {
        return isRegistered;
    }
}
