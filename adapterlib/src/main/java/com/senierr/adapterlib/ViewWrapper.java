package com.senierr.adapterlib;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.senierr.adapterlib.listener.OnChildClickListener;
import com.senierr.adapterlib.listener.OnClickListener;
import com.senierr.adapterlib.listener.OnItemClickListener;
import com.senierr.adapterlib.util.RVHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public abstract class ViewWrapper<T> {

    private OnClickListener onClickListener;
    private  List<OnChildClickListener> onChildClickListeners;

    public abstract @LayoutRes int getLayoutId();

    public abstract void onBindViewHolder(@NonNull RVHolder holder, @NonNull T item);

    @NonNull
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        final RVHolder holder = RVHolder.create(parent, getLayoutId());
        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(holder, holder.getLayoutPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return onClickListener.onLongClick(holder, holder.getLayoutPosition());
                }
            });
        }

        if (onChildClickListeners != null) {
            for (final OnChildClickListener onChildClickListener : onChildClickListeners) {
                View childView = holder.getView(onChildClickListener.childId);
                if (childView != null) {
                    childView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onChildClickListener.onClick(holder, view, holder.getLayoutPosition());
                        }
                    });
                    childView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            return onChildClickListener.onLongClick(holder, view, holder.getLayoutPosition());
                        }
                    });
                }
            }
        }
        return holder;
    }

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

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OnChildClickListener getOnChildClickListener(@IdRes int childId) {
        if (onChildClickListeners != null) {
            for (OnChildClickListener onChildClickListener : onChildClickListeners) {
                if(onChildClickListener.childId == childId) {
                    return onChildClickListener;
                }
            }
        }
        return null;
    }

    public void setOnChildClickListener(@NonNull OnChildClickListener onChildClickListener) {
        if (onChildClickListeners == null) {
            onChildClickListeners = new ArrayList<>();
        }
        onChildClickListeners.add(onChildClickListener);
    }
}
