package com.senierr.adapterlib;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.senierr.adapterlib.util.RVHolder;

import java.util.List;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public abstract class ViewWrapper<T> {

    public abstract RVHolder onCreateViewHolder(@NonNull ViewGroup parent);

    public abstract void onBindViewHolder(@NonNull RVHolder holder, @NonNull T item);

    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull T item, @NonNull List<Object> payloads) {
        onBindViewHolder(holder, item);
    }

    public long getItemId(T item) {
        return RecyclerView.NO_ID;
    }

    public void onViewRecycled(RVHolder holder) {}

    public boolean onFailedToRecycleView(RVHolder holder) {
        return false;
    }

    public void onViewAttachedToWindow(RVHolder holder) {}

    public void onViewDetachedFromWindow(RVHolder holder) {}

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {}

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {}

    public int getSpanSize(T item) {
        return 1;
    }
}
