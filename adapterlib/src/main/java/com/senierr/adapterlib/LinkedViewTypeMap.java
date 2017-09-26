package com.senierr.adapterlib;

import android.support.annotation.NonNull;

import com.senierr.adapterlib.binder.Binder;

import java.util.ArrayList;
import java.util.List;

/**
 * 有序数据-视图类型关系集合
 *
 * key: 为数据类型
 * value: 为视图类型
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class LinkedViewTypeMap {

    // 数据类型
    private List<Class<?>> classes;
    // 布局类型
    private List<ViewWrapper<?>> wrappers;
    // 对应关系
    private List<Binder<?>> binders;

    public LinkedViewTypeMap() {
        this.classes = new ArrayList<>();
        this.wrappers = new ArrayList<>();
        this.binders = new ArrayList<>();
    }

    public void put(@NonNull Class<?> cls, @NonNull ViewWrapper<?> wrapper, @NonNull Binder<?> binder) {
        classes.add(cls);
        wrappers.add(wrapper);
        binders.add(binder);
    }

    public void remove(@NonNull Class<?> cls) {
        while (true) {
            int index = classes.indexOf(cls);
            if (index != -1) {
                classes.remove(index);
                binders.remove(index);
                binders.remove(index);
            } else {
                break;
            }
        }
    }

    public int indexOf(@NonNull Class<?> cls) {
        return classes.indexOf(cls);
    }

    public int size() {
        return classes.size();
    }

    @NonNull
    public Class<?> getClass(int index) {
        return classes.get(index);
    }

    @NonNull
    public ViewWrapper<?> getViewWrapper(int index) {
        return wrappers.get(index);
    }

    @NonNull
    public Binder<?> getBinder(int index) {
        return binders.get(index);
    }

    @NonNull
    public List<Class<?>> getClasses() {
        return classes;
    }

    @NonNull
    public List<ViewWrapper<?>> getViewWrappers() {
        return wrappers;
    }

    @NonNull
    public List<Binder<?>> getBinders() {
        return binders;
    }
}
