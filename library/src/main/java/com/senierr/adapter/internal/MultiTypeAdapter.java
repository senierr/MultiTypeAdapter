package com.senierr.adapter.internal;

import android.support.annotation.CheckResult;
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
    private List<ViewHolderWrapper<?>> wrapperList;
    // 数据类型对应的连接器
    private Map<Class<?>, Linker<?>> linkerMap;

    public MultiTypeAdapter() {
        this(new ArrayList<>());
    }

    public MultiTypeAdapter(@NonNull List<Object> data) {
        this.data = data;
        wrapperList = new ArrayList<>();
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
        ViewHolderWrapper viewHolderWrapper = linker.getItemViewType(item);
        // 根据视图类型转换为Type
        int itemViewType = wrapperList.indexOf(viewHolderWrapper);
        if (itemViewType < 0 || itemViewType > wrapperList.size() - 1) {
            throw new UnregisteredException(viewHolderWrapper.getClass());
        }
        return itemViewType;
    }

    @Override
    public final ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolderWrapper<?> viewHolderWrapper = wrapperList.get(viewType);
        return viewHolderWrapper.generateViewHolder(parent, this);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onBindViewHolder(ViewHolder holder, int position, @NonNull List<Object> payloads) {
        Object item = data.get(position);
        ViewHolderWrapper viewHolderWrapper = wrapperList.get(holder.getItemViewType());
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
        ViewHolderWrapper viewHolderWrapper = wrapperList.get(getItemViewType(position));
        return viewHolderWrapper.getItemId(data.get(position));
    }

    @Override
    public final void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        ViewHolderWrapper viewHolderWrapper = wrapperList.get(holder.getItemViewType());
        viewHolderWrapper.onViewRecycled(holder);
    }

    @Override
    public final boolean onFailedToRecycleView(ViewHolder holder) {
        ViewHolderWrapper viewHolderWrapper = wrapperList.get(holder.getItemViewType());
        return viewHolderWrapper.onFailedToRecycleView(holder);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (recyclerView == null) {
            return;
        }

        ViewHolderWrapper viewHolderWrapper = wrapperList.get(holder.getItemViewType());
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
        ViewHolderWrapper viewHolderWrapper = wrapperList.get(holder.getItemViewType());
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
                    ViewHolderWrapper viewHolderWrapper = wrapperList.get(getItemViewType(position));
                    int spanSize = viewHolderWrapper.getSpanSize(data.get(position));
                    int spanCount = gridManager.getSpanCount();
                    return spanSize > spanCount ? spanCount : spanSize;
                }
            });
        }

        for (ViewHolderWrapper<?> viewHolderWrapper : wrapperList) {
            viewHolderWrapper.onAttachedToRecyclerView(recyclerView);
        }
    }

    @Override
    public final void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;

        for (ViewHolderWrapper<?> viewHolderWrapper : wrapperList) {
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

    public final <T> void register(@NonNull Class<T> cls, @NonNull final ViewHolderWrapper<T> viewHolderWrapper) {
        new RegisterHelper<>(cls, viewHolderWrapper).with(new Linker<T>() {
            @NonNull
            @Override
            public ViewHolderWrapper<T> getItemViewType(@NonNull T item) {
                return viewHolderWrapper;
            }
        });
    }

    @NonNull @SafeVarargs @CheckResult
    public final <T> RegisterHelper<T> register(@NonNull Class<T> cls, @NonNull ViewHolderWrapper<T>... viewHolderWrappers) {
        return new RegisterHelper<>(cls, viewHolderWrappers);
    }

    public final class RegisterHelper<T> {

        private Class<T> cls;
        private ViewHolderWrapper<T>[] viewHolderWrappers;

        @SafeVarargs
        RegisterHelper(Class<T> cls, ViewHolderWrapper<T>... viewHolderWrappers) {
            this.cls = cls;
            this.viewHolderWrappers = viewHolderWrappers;
        }

        public final void with(@NonNull Linker<T> linker) {
            for (ViewHolderWrapper<T> viewHolderWrapper : viewHolderWrappers) {
                viewHolderWrapper.setMultiTypeAdapter(MultiTypeAdapter.this);
                wrapperList.add(viewHolderWrapper);
            }
            linkerMap.put(cls, linker);
        }
    }
}
