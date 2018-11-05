package com.senierr.adapter.internal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多类型适配器
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */
public class MultiTypeAdapter extends RecyclerView.Adapter<ViewHolder> {

    private @Nullable RecyclerView recyclerView;
    private @NonNull List<Object> data;
    // 视图集合：对应的Index即为ItemViewType
    private List<ViewHolderWrapper<?>> viewHolderWrappers;
    // 数据类型对应的连接器
    private Map<Class<?>, Linker<?>> linkerMap;

    public MultiTypeAdapter() {
        this(new ArrayList<>());
    }

    public MultiTypeAdapter(@NonNull List<Object> data) {
        this.data = data;
        viewHolderWrappers = new ArrayList<>();
        linkerMap = new HashMap<>();
    }

    @Override @SuppressWarnings("unchecked")
    public final int getItemViewType(int position) {
        Object item = data.get(position);
        // 获取数据类型对应的连接器
        Linker linker = linkerMap.get(item.getClass());
        if (linker == null) {
            throw new UnregisteredException(item.getClass());
        }
        // 根据连接器，获取对应的视图类型
        Class cls = linker.getItemViewType(item);
        // 根据视图类型转换为Type
        int itemViewType = -1;
        for (int i = 0; i < viewHolderWrappers.size(); i++) {
            if (viewHolderWrappers.get(i).getClass().equals(cls)) {
                itemViewType = i;
                break;
            }
        }
        if (itemViewType == -1) {
            throw new UnregisteredException(cls);
        }
        return itemViewType;
    }

    @Override
    public final ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolderWrapper<?> viewHolderWrapper = viewHolderWrappers.get(viewType);
        return viewHolderWrapper.generateViewHolder(parent, this);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onBindViewHolder(ViewHolder holder, int position, @NonNull List<Object> payloads) {
        Object item = data.get(position);
        ViewHolderWrapper viewHolderWrapper = viewHolderWrappers.get(holder.getItemViewType());
        viewHolderWrapper.onBindViewHolder(holder, item, payloads);
    }

    @Override @Deprecated
    public final void onBindViewHolder(@NonNull ViewHolder holder, int position) {}

    @Override
    public final int getItemCount() {
        return data.size();
    }

    @Override @SuppressWarnings("unchecked")
    public final long getItemId(int position) {
        ViewHolderWrapper viewHolderWrapper = viewHolderWrappers.get(getItemViewType(position));
        return viewHolderWrapper.getItemId(data.get(position));
    }

    @Override
    public final void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        ViewHolderWrapper viewHolderWrapper = viewHolderWrappers.get(holder.getItemViewType());
        viewHolderWrapper.onViewRecycled(holder);
    }

    @Override
    public final boolean onFailedToRecycleView(ViewHolder holder) {
        ViewHolderWrapper viewHolderWrapper = viewHolderWrappers.get(holder.getItemViewType());
        return viewHolderWrapper.onFailedToRecycleView(holder);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (recyclerView == null) {
            return;
        }

        ViewHolderWrapper viewHolderWrapper = viewHolderWrappers.get(holder.getItemViewType());
        viewHolderWrapper.onViewAttachedToWindow(holder);

        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager == null) return;
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int position = holder.getLayoutPosition();
            int spanSize = viewHolderWrapper.getSpanSize(data.get(position));
            p.setFullSpan(spanSize >= staggeredGridLayoutManager.getSpanCount());
        }
    }

    @Override
    public final void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ViewHolderWrapper viewHolderWrapper = viewHolderWrappers.get(holder.getItemViewType());
        viewHolderWrapper.onViewDetachedFromWindow(holder);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    ViewHolderWrapper viewHolderWrapper = viewHolderWrappers.get(getItemViewType(position));
                    int spanSize = viewHolderWrapper.getSpanSize(data.get(position));
                    int spanCount = gridManager.getSpanCount();
                    return spanSize > spanCount ? spanCount : spanSize;
                }
            });
        }

        for (ViewHolderWrapper<?> viewHolderWrapper : viewHolderWrappers) {
            viewHolderWrapper.onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override
    public final void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;

        for (ViewHolderWrapper<?> viewHolderWrapper : viewHolderWrappers) {
            viewHolderWrapper.onDetachedFromRecyclerView(recyclerView);
        }
    }

    @NonNull
    public List<Object> getData() {
        return data;
    }

    public void setData(@NonNull List<Object> data) {
        this.data = data;
    }

    public <T> void register(@NonNull Class<T> cls, @NonNull final ViewHolderWrapper<T> viewHolderWrapper) {
        List<ViewHolderWrapper<T>> list = new ArrayList<>();
        list.add(viewHolderWrapper);
        register(cls, list, new Linker<T>() {
            @Override
            public Class<? extends ViewHolderWrapper> getItemViewType(@NonNull T item) {
                return viewHolderWrapper.getClass();
            }
        });
    }

    public <T> void register(@NonNull Class<T> cls,
                             @NonNull List<ViewHolderWrapper<T>> wrapperList,
                             @NonNull Linker<T> linker) {
        for (ViewHolderWrapper<T> viewHolderWrapper : wrapperList) {
            viewHolderWrapper.setMultiTypeAdapter(this);
            viewHolderWrappers.add(viewHolderWrapper);
        }
        linkerMap.put(cls, linker);
    }
}
