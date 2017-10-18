package com.senierr.adapter.core;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhouchunjie
 * @date 2017/10/10
 */

public class WrapperPool {

    private List<ViewHolderWrapper<?>> viewHolderWrappers;
    private List<DataBinder<?>> dataBinders;

    public WrapperPool() {
        viewHolderWrappers = new ArrayList<>();
        dataBinders = new ArrayList<>();
    }

    public void add(@NonNull ViewHolderWrapper<?> viewHolderWrapper, @NonNull DataBinder<?> dataBinder) {
        viewHolderWrappers.add(viewHolderWrapper);
        dataBinders.add(dataBinder);
    }

    public void remove(@NonNull ViewHolderWrapper<?> viewHolderWrapper, @NonNull DataBinder<?> dataBinder) {
        viewHolderWrappers.remove(viewHolderWrapper);
        dataBinders.remove(dataBinder);
    }

    @SuppressWarnings("unchecked")
    public <T> int getViewHolderWrapperIndex(@NonNull T item) {
        for (int i = 0; i < viewHolderWrappers.size(); i++) {
            ViewHolderWrapper<?> viewHolderWrapper = viewHolderWrappers.get(i);
            if (viewHolderWrapper.getDataCls().equals(item.getClass())) {
                DataBinder<T> dataBinder = (DataBinder<T>) dataBinders.get(i);
                ViewHolderWrapper<T> wrapper = dataBinder.getViewHolderWrapper(item);
                if (wrapper.equals(viewHolderWrapper)) {
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
