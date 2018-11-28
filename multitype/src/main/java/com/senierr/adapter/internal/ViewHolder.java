package com.senierr.adapter.internal;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> viewSparseArray;

    private ViewHolder(View itemView) {
        super(itemView);
        viewSparseArray = new SparseArray<>();
    }

    @NonNull
    public static ViewHolder create(@NonNull ViewGroup parent, @LayoutRes int layoutId) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return create(itemView);
    }

    @NonNull
    public static ViewHolder create(@NonNull LayoutInflater layoutInflater,
                                    @NonNull ViewGroup parent, @LayoutRes int layoutId) {
        View itemView = layoutInflater.inflate(layoutId, parent, false);
        return create(itemView);
    }

    @NonNull
    public static ViewHolder create(@NonNull View itemView) {
        return new ViewHolder(itemView);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findView(@IdRes int viewId) {
        View view = viewSparseArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            viewSparseArray.put(viewId, view);
        }
        return (T) view;
    }
}
