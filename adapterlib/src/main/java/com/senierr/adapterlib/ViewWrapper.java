package com.senierr.adapterlib;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.senierr.adapterlib.listener.OnItemChildClickListener;
import com.senierr.adapterlib.listener.OnItemClickListener;
import com.senierr.adapterlib.util.RVHolder;

import java.util.List;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public abstract class ViewWrapper<T> {

    private @Nullable OnItemClickListener onItemClickListener;
    private @Nullable SparseArray<OnItemChildClickListener> onItemChildClickListeners;

    public abstract @LayoutRes int getLayoutId();

    public abstract void onBindViewHolder(@NonNull RVHolder holder, @NonNull T item);

    @NonNull
    public RVHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        final RVHolder holder = RVHolder.create(parent, getLayoutId());
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onClick(holder, holder.getLayoutPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return onItemClickListener.onLongClick(holder, holder.getLayoutPosition());
                }
            });
        }

        if (onItemChildClickListeners != null) {
            for (int i = 0; i < onItemChildClickListeners.size(); i++) {
                int key = onItemChildClickListeners.keyAt(i);
                final OnItemChildClickListener onItemChildClickListener = onItemChildClickListeners.get(key);
                View childView = holder.getView(key);
                if (childView != null && onItemChildClickListener != null) {
                    childView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onItemChildClickListener.onClick(holder, view, holder.getLayoutPosition());
                        }
                    });
                    childView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            return onItemChildClickListener.onLongClick(holder, view, holder.getLayoutPosition());
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

    @Nullable
    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Nullable
    public OnItemChildClickListener getOnItemChildClickListener(@IdRes int childId) {
        if (onItemChildClickListeners != null) {
            return onItemChildClickListeners.get(childId);
        }
        return null;
    }

    public void setOnItemChildClickListener(@IdRes int childId, @NonNull OnItemChildClickListener onItemChildClickListener) {
        if (onItemChildClickListeners == null) {
            onItemChildClickListeners = new SparseArray<>();
        }
        onItemChildClickListeners.put(childId, onItemChildClickListener);
    }
}
