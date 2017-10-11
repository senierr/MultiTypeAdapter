package com.senierr.adapter.util;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 通用ViewHolder
 *
 * @author zhouchunjie
 * @date 2017/9/20
 */

public class RVHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> viewSparseArray;

    private RVHolder(View itemView) {
        super(itemView);
        viewSparseArray = new SparseArray<>();
    }

    @NonNull
    public static RVHolder create(@NonNull ViewGroup parent, @LayoutRes int layoutId) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new RVHolder(itemView);
    }

    @NonNull
    public static RVHolder create(@NonNull View itemView) {
        return new RVHolder(itemView);
    }

    /**
     * 通过id获得控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = viewSparseArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            viewSparseArray.put(viewId, view);
        }
        return (T) view;
    }

    public RVHolder setText(@IdRes int viewId, @NonNull CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public RVHolder setText(@IdRes int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        view.setText(strId);
        return this;
    }

    public RVHolder setGone(@IdRes int viewId, boolean isGone) {
        View view = getView(viewId);
        view.setVisibility(isGone ? View.GONE : View.VISIBLE);
        return this;
    }

    public RVHolder setVisible(@IdRes int viewId, boolean isVisible) {
        View view = getView(viewId);
        view.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
        return this;
    }
}
