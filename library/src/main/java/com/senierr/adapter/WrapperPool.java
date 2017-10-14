package com.senierr.adapter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 产线池
 *
 * @author zhouchunjie
 * @date 2017/10/10
 */

public class WrapperPool {

    private List<ViewHolderWrapper<?>> viewHolderWrappers;

    public WrapperPool() {
        viewHolderWrappers = new ArrayList<>();
    }

    /**
     * 添加产线
     *
     * @param viewHolderWrapper
     */
    public void addViewHolderWrapper(@NonNull ViewHolderWrapper<?> viewHolderWrapper) {
        this.viewHolderWrappers.add(viewHolderWrapper);
    }

    /**
     * 移除产线
     *
     * @param viewHolderWrapper
     */
    public void removeViewHolderWrapper(@NonNull ViewHolderWrapper<?> viewHolderWrapper) {
        this.viewHolderWrappers.remove(viewHolderWrapper);
    }

    /**
     * 获取处理该数据的产线的索引
     *
     * @param item 待处理数据
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> int getViewHolderWrapperIndex(@NonNull T item) {
        for (int i = 0; i < viewHolderWrappers.size(); i++) {
            ViewHolderWrapper<?> viewHolderWrapper = viewHolderWrappers.get(i);
            if (viewHolderWrapper.getDataCls().equals(item.getClass())) {
                if (((ViewHolderWrapper<T>) viewHolderWrapper).onAcceptAssignment(item)) {
                    return i;
                }
            }
        }
        throw new IllegalArgumentException(String.format(
                "Can not found ViewHolderWrapper for %s!", item.getClass().getSimpleName()));
    }

    public ViewHolderWrapper<?> getViewHolderWrapper(int index) {
        return viewHolderWrappers.get(index);
    }

    public List<ViewHolderWrapper<?>> getViewHolderWrappers() {
        return viewHolderWrappers;
    }
}
