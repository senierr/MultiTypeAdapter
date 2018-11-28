package com.senierr.adapter.internal;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.senierr.adapter.listener.OnChildClickListener;
import com.senierr.adapter.listener.OnChildLongClickListener;
import com.senierr.adapter.listener.OnItemClickListener;
import com.senierr.adapter.listener.OnItemLongClickListener;

import java.util.List;

/**
 * 视图-数据封装
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */
public abstract class ViewHolderWrapper<T> {

    private @Nullable MultiTypeAdapter multiTypeAdapter;
    private @Nullable OnItemClickListener<T> onItemClickListener;
    private @Nullable OnItemLongClickListener<T> onItemLongClickListener;
    private @Nullable SparseArray<OnChildClickListener<T>> onChildClickListeners;
    private @Nullable SparseArray<OnChildLongClickListener<T>> onChildLongClickListeners;

    @NonNull
    final ViewHolder generateViewHolder(@NonNull ViewGroup parent,
                                        @NonNull MultiTypeAdapter multiTypeAdapter) {
        setMultiTypeAdapter(multiTypeAdapter);
        // 创建视图
        final ViewHolder holder = onCreateViewHolder(parent);
        // 绑定列表项点击事件
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getLayoutPosition();
                    T t = getItemData(position);
                    if (t != null) {
                        onItemClickListener.onClick(holder, position, t);
                    }
                }
            });
        }
        // 绑定列表项长按事件
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = holder.getLayoutPosition();
                    T t = getItemData(position);
                    return t != null && onItemLongClickListener.onLongClick(holder, position, t);
                }
            });
        }
        // 绑定子视图点击事件
        if (onChildClickListeners != null && onChildClickListeners.size() > 0) {
            for (int i = 0; i < onChildClickListeners.size(); i++) {
                int key = onChildClickListeners.keyAt(i);
                final OnChildClickListener<T> onChildClickListener = onChildClickListeners.get(key);
                View childView = holder.findView(key);
                if (onChildClickListener != null && childView != null) {
                    childView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = holder.getLayoutPosition();
                            T t = getItemData(position);
                            if (t != null) {
                                onChildClickListener.onClick(holder, view, position, t);
                            }
                        }
                    });
                }
            }
        }
        // 绑定子视图长按事件
        if (onChildLongClickListeners != null && onChildLongClickListeners.size() > 0) {
            for (int i = 0; i < onChildLongClickListeners.size(); i++) {
                int key = onChildLongClickListeners.keyAt(i);
                final OnChildLongClickListener<T> onChildLongClickListener = onChildLongClickListeners.get(key);
                View childView = holder.findView(key);
                if (onChildLongClickListener != null && childView != null) {
                    childView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int position = holder.getLayoutPosition();
                            T t = getItemData(position);
                            return t != null && onChildLongClickListener.onLongClick(holder, v, position, t);
                        }
                    });
                }
            }
        }
        return holder;
    }

    final void setMultiTypeAdapter(@NonNull MultiTypeAdapter multiTypeAdapter) {
        this.multiTypeAdapter = multiTypeAdapter;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private T getItemData(int position) {
        if (multiTypeAdapter == null) return null;
        try {
            return (T) multiTypeAdapter.getData().get(position);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    public final MultiTypeAdapter getMultiTypeAdapter() {
        return multiTypeAdapter;
    }

    public long getItemId(T item) {
        return RecyclerView.NO_ID;
    }

    public void onViewRecycled(ViewHolder holder) {}

    public boolean onFailedToRecycleView(ViewHolder holder) {
        return false;
    }

    public void onViewAttachedToWindow(ViewHolder holder) {}

    public void onViewDetachedFromWindow(ViewHolder holder) {}

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {}

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {}

    @NonNull
    public abstract ViewHolder onCreateViewHolder(@NonNull ViewGroup parent);

    public abstract void onBindViewHolder(@NonNull ViewHolder holder, @NonNull T item);

    public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull T item, @NonNull List<Object> payloads) {
        onBindViewHolder(holder, item);
    }

    /**
     * 获取当前项所占列数
     *
     * @param item 数据项
     * @return 默认为1，超过最大列数为全行
     */
    public int getSpanSize(T item) {
        return 1;
    }

    @Nullable
    public final OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Nullable
    public final OnItemLongClickListener<T> getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public final void setOnItemLongClickListener(@Nullable OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Nullable
    public final OnChildClickListener<T> getOnChildClickListener(@IdRes int childId) {
        if (onChildClickListeners == null) return null;
        return onChildClickListeners.get(childId);
    }

    public final void setOnChildClickListener(@IdRes int childId, @Nullable OnChildClickListener<T> onChildClickListener) {
        if (onChildClickListeners == null) {
            onChildClickListeners = new SparseArray<>();
        }
        onChildClickListeners.put(childId, onChildClickListener);
    }

    @Nullable
    public final OnChildLongClickListener<T> getOnChildLongClickListener(@IdRes int childId) {
        if (onChildLongClickListeners == null) return null;
        return onChildLongClickListeners.get(childId);
    }

    public final void setOnChildLongClickListener(@IdRes int childId, @Nullable OnChildLongClickListener<T> onChildLongClickListener) {
        if (onChildLongClickListeners == null) {
            onChildLongClickListeners = new SparseArray<>();
        }
        onChildLongClickListeners.put(childId, onChildLongClickListener);
    }
}
