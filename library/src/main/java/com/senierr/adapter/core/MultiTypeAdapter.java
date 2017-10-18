package com.senierr.adapter.core;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class MultiTypeAdapter extends RecyclerView.Adapter<RVHolder> {

    private @Nullable RecyclerView recyclerView;
    private @NonNull WrapperPool wrapperPool;
    private @NonNull List<Object> dataList;

    public MultiTypeAdapter() {
        this(new ArrayList<>());
    }

    public MultiTypeAdapter(@NonNull List<Object> dataList) {
        this.wrapperPool = new WrapperPool();
        this.dataList = dataList;
    }

    @Override @SuppressWarnings("unchecked")
    public final int getItemViewType(int position) {
        Object item = dataList.get(position);
        return wrapperPool.getViewHolderWrapperIndex(item);
    }

    @Override
    public final RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderWrapper<?> viewHolderWrapper = wrapperPool.getViewHolderWrapper(viewType);
        return viewHolderWrapper.onCreateViewHolder(parent);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onBindViewHolder(RVHolder holder, int position, List<Object> payloads) {
        Object item = dataList.get(position);
        ViewHolderWrapper<Object> viewHolderWrapper =
                (ViewHolderWrapper<Object>) wrapperPool.getViewHolderWrapper(holder.getItemViewType());
        viewHolderWrapper.onBindViewHolder(holder, item, payloads);
    }

    @Override @Deprecated
    public final void onBindViewHolder(RVHolder holder, int position) {}

    @Override
    public final int getItemCount() {
        return dataList.size();
    }

    @Override @SuppressWarnings("unchecked")
    public final long getItemId(int position) {
        ViewHolderWrapper viewHolderWrapper = wrapperPool.getViewHolderWrapper(getItemViewType(position));
        return viewHolderWrapper.getItemId(dataList.get(position));
    }

    @Override
    public final void onViewRecycled(RVHolder holder) {
        super.onViewRecycled(holder);
        ViewHolderWrapper viewHolderWrapper = wrapperPool.getViewHolderWrapper(holder.getItemViewType());
        viewHolderWrapper.onViewRecycled(holder);
    }

    @Override
    public final boolean onFailedToRecycleView(RVHolder holder) {
        ViewHolderWrapper viewHolderWrapper = wrapperPool.getViewHolderWrapper(holder.getItemViewType());
        return viewHolderWrapper.onFailedToRecycleView(holder);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onViewAttachedToWindow(RVHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (recyclerView == null) {
            return;
        }

        ViewHolderWrapper viewHolderWrapper = wrapperPool.getViewHolderWrapper(holder.getItemViewType());
        viewHolderWrapper.onViewAttachedToWindow(holder);

        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int position = holder.getLayoutPosition();
            int spanSize = viewHolderWrapper.getSpanSize(dataList.get(position));
            p.setFullSpan(spanSize >= staggeredGridLayoutManager.getSpanCount());
        }
    }

    @Override
    public final void onViewDetachedFromWindow(RVHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ViewHolderWrapper viewHolderWrapper = wrapperPool.getViewHolderWrapper(holder.getItemViewType());
        viewHolderWrapper.onViewDetachedFromWindow(holder);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;

        List<ViewHolderWrapper<?>> viewHolderWrappers = wrapperPool.getViewHolderWrappers();
        for (ViewHolderWrapper<?> viewHolderWrapper : viewHolderWrappers) {
            viewHolderWrapper.onAttachedToRecyclerView(recyclerView);
        }

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    ViewHolderWrapper viewHolderWrapper = wrapperPool.getViewHolderWrapper(getItemViewType(position));
                    int spanSize = viewHolderWrapper.getSpanSize(dataList.get(position));
                    int spanCount = gridManager.getSpanCount();
                    return spanSize > spanCount ? spanCount : spanSize;
                }
            });
        }
    }

    @Override
    public final void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;

        List<ViewHolderWrapper<?>> viewHolderWrappers = wrapperPool.getViewHolderWrappers();
        for (ViewHolderWrapper<?> viewHolderWrapper : viewHolderWrappers) {
            viewHolderWrapper.onDetachedFromRecyclerView(recyclerView);
        }
    }

    @NonNull @SafeVarargs @CheckResult
    public final <T> RegisterHelper<T> register(@NonNull ViewHolderWrapper<T>... viewHolderWrappers) {
        for (ViewHolderWrapper<?> viewHolderWrapper : viewHolderWrappers) {
            viewHolderWrapper.setMultiTypeAdapter(this);
        }
        return new RegisterHelper<T>().register(viewHolderWrappers);
    }

    public final <T> void register(@NonNull ViewHolderWrapper<T> viewHolderWrapper) {
        viewHolderWrapper.setMultiTypeAdapter(this);
        new RegisterHelper<T>().register(viewHolderWrapper).with(new DataBinder<T>() {
            @Override
            public int onBindIndex(@NonNull T item) {
                return 0;
            }
        });
    }

    /**
     * 获取数据集
     *
     * @return
     */
    @NonNull
    public List<Object> getDataList() {
        return dataList;
    }

    /**
     * 设置数据集
     *
     * @param dataList
     */
    public void setDataList(@NonNull List<Object> dataList) {
        this.dataList = dataList;
    }

    /**
     * 注册帮助类
     *
     * @param <T>
     */
    public final class RegisterHelper<T> {

        private ViewHolderWrapper<T>[] viewHolderWrappers;

        @NonNull @CheckResult @SafeVarargs
        final RegisterHelper<T> register(@NonNull ViewHolderWrapper<T>... viewHolderWrappers) {
            this.viewHolderWrappers = viewHolderWrappers;
            return this;
        }

        public final void with(@NonNull DataBinder<T> dataBinder) {
            dataBinder.setViewHolderWrappers(viewHolderWrappers);
            for (ViewHolderWrapper<T> viewHolderWrapper : viewHolderWrappers) {
                wrapperPool.add(viewHolderWrapper, dataBinder);
            }
        }
    }
}
