package com.senierr.adapter.internal;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 将指定类型的数据和视图生成为ViewHolder
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */

public abstract class ViewHolderWrapper<T> {

    private @Nullable
    MultiTypeAdapter multiTypeAdapter;
    private @Nullable OnItemClickListener onItemClickListener;
    private @Nullable SparseArray<OnItemChildClickListener> onItemChildClickListeners;

    @NonNull
    final RVHolder getViewHolder(@NonNull ViewGroup parent) {
        final RVHolder holder = onCreateViewHolder(parent);
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

    @NonNull
    public abstract  RVHolder onCreateViewHolder(@NonNull ViewGroup parent);

    public abstract void onBindViewHolder(@NonNull RVHolder holder, @NonNull T item);

    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull T item, @NonNull List<Object> payloads) {
        onBindViewHolder(holder, item);
    }

    public final void setAdapter(@Nullable MultiTypeAdapter multiTypeAdapter) {
        this.multiTypeAdapter = multiTypeAdapter;
    }

    @Nullable
    public final MultiTypeAdapter getAdapter() {
        return multiTypeAdapter;
    }

    @Nullable
    public final OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public final void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Nullable
    public final OnItemChildClickListener getOnItemChildClickListener(@IdRes int childId) {
        if (onItemChildClickListeners != null) {
            return onItemChildClickListeners.get(childId);
        }
        return null;
    }

    public final void setOnItemChildClickListener(@IdRes int childId, @NonNull OnItemChildClickListener onItemChildClickListener) {
        if (onItemChildClickListeners == null) {
            onItemChildClickListeners = new SparseArray<>();
        }
        onItemChildClickListeners.put(childId, onItemChildClickListener);
    }

    /**
     * 列表项点击事件
     */
    public static abstract class OnItemClickListener {

        public void onClick(RVHolder viewHolder, int position) {}

        public boolean onLongClick(RVHolder viewHolder, int position) {
            return false;
        }
    }

    /**
     * 子控件点击事件
     */
    public static abstract class OnItemChildClickListener {

        public void onClick(RVHolder viewHolder, View view, int position) {}

        public boolean onLongClick(RVHolder viewHolder, View view, int position) {
            return false;
        }
    }
}
