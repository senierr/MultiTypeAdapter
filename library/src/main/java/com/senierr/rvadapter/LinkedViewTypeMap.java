package com.senierr.rvadapter;

import android.support.annotation.NonNull;

import com.senierr.rvadapter.link.DefaultLink;

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
    private List<ViewHolderWrapper<?>> viewHolderWrappers;
    // 对应关系
    private List<DefaultLink<?>> links;

    public LinkedViewTypeMap() {
        this.classes = new ArrayList<>();
        this.viewHolderWrappers = new ArrayList<>();
        this.links = new ArrayList<>();
    }

    public void put(@NonNull Class<?> cls, @NonNull ViewHolderWrapper<?> viewHolderWrapper, @NonNull DefaultLink<?> link) {
        classes.add(cls);
        viewHolderWrappers.add(viewHolderWrapper);
        links.add(link);
    }

    public void remove(@NonNull Class<?> cls) {
        while (true) {
            int index = classes.indexOf(cls);
            if (index != -1) {
                classes.remove(index);
                links.remove(index);
                links.remove(index);
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
    public ViewHolderWrapper<?> getViewHolderWrapper(int index) {
        return viewHolderWrappers.get(index);
    }

    @NonNull
    public DefaultLink<?> getLink(int index) {
        return links.get(index);
    }

    @NonNull
    public List<Class<?>> getClasses() {
        return classes;
    }

    @NonNull
    public List<ViewHolderWrapper<?>> getViewHolderWrappers() {
        return viewHolderWrappers;
    }

    @NonNull
    public List<DefaultLink<?>> getLinks() {
        return links;
    }
}
