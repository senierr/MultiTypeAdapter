package com.senierr.adapterlib;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.senierr.adapterlib.binder.Binder;
import com.senierr.adapterlib.exception.WrapperNotFoundException;
import com.senierr.adapterlib.util.RVHolder;

import java.util.Collections;
import java.util.List;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class RVAdapter extends RecyclerView.Adapter<RVHolder> {

    private @Nullable RecyclerView recyclerView;
    private @NonNull LinkedViewTypeMap linkedViewTypeMap;
    private @NonNull List<?> items;

    public RVAdapter() {
        this(new LinkedViewTypeMap(), Collections.emptyList());
    }

    public RVAdapter(@NonNull LinkedViewTypeMap linkedViewTypeMap, @NonNull List<Object> items) {
        this.linkedViewTypeMap = linkedViewTypeMap;
        this.items = items;
    }

    @Override
    public final RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderWrapper<?> viewWrapper = linkedViewTypeMap.getViewWrapper(viewType);
        return viewWrapper.onCreateViewHolder(parent);
    }

    @Override @Deprecated
    public final void onBindViewHolder(RVHolder holder, int position) {}

    @Override @SuppressWarnings("unchecked")
    public final void onBindViewHolder(RVHolder holder, int position, List<Object> payloads) {
        Object item = items.get(position);
        ViewHolderWrapper<Object> wrapper = (ViewHolderWrapper<Object>) linkedViewTypeMap.getViewWrapper(holder.getItemViewType());
        wrapper.onBindViewHolder(holder, item, payloads);
    }

    @Override
    public final int getItemCount() {
        return items.size();
    }

    @Override @SuppressWarnings("unchecked")
    public final int getItemViewType(int position) {
        Object item = items.get(position);
        int classIndex = linkedViewTypeMap.indexOf(item.getClass());
        if (classIndex == -1) {
            throw new WrapperNotFoundException(item.getClass());
        }
        Binder<Object> binder = (Binder<Object>) linkedViewTypeMap.getBinder(classIndex);
        return classIndex + binder.index(item);
    }

    @Override @SuppressWarnings("unchecked")
    public final long getItemId(int position) {
        ViewHolderWrapper wrapper = linkedViewTypeMap.getViewWrapper(getItemViewType(position));
        return wrapper.getItemId(items.get(position));
    }

    @Override
    public final void onViewRecycled(RVHolder holder) {
        super.onViewRecycled(holder);
        ViewHolderWrapper wrapper = linkedViewTypeMap.getViewWrapper(holder.getItemViewType());
        wrapper.onViewRecycled(holder);
    }

    @Override
    public final boolean onFailedToRecycleView(RVHolder holder) {
        ViewHolderWrapper wrapper = linkedViewTypeMap.getViewWrapper(holder.getItemViewType());
        return wrapper.onFailedToRecycleView(holder);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onViewAttachedToWindow(RVHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (recyclerView ==null) {
            return;
        }

        ViewHolderWrapper wrapper = linkedViewTypeMap.getViewWrapper(holder.getItemViewType());
        wrapper.onViewAttachedToWindow(holder);

        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int position = holder.getLayoutPosition();
            int spanSize = wrapper.getSpanSize(items.get(position));
            p.setFullSpan(spanSize >= staggeredGridLayoutManager.getSpanCount());
        }
    }

    @Override
    public final void onViewDetachedFromWindow(RVHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ViewHolderWrapper wrapper = linkedViewTypeMap.getViewWrapper(holder.getItemViewType());
        wrapper.onViewDetachedFromWindow(holder);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;

        List<ViewHolderWrapper<?>> wrappers = linkedViewTypeMap.getViewWrappers();
        for (ViewHolderWrapper<?> wrapper : wrappers) {
            wrapper.onAttachedToRecyclerView(recyclerView);
        }

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    ViewHolderWrapper wrapper = linkedViewTypeMap.getViewWrapper(getItemViewType(position));
                    int spanSize = wrapper.getSpanSize(items.get(position));
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

        List<ViewHolderWrapper<?>> wrappers = linkedViewTypeMap.getViewWrappers();
        for (ViewHolderWrapper<?> wrapper : wrappers) {
            wrapper.onDetachedFromRecyclerView(recyclerView);
        }
    }

    public final <T> RVAdapter addViewHolderWrapper(@NonNull ViewHolderWrapper<T> viewHolderWrapper) {

        return this;
    }

    @CheckResult
    public final <T> RVAdapter addViewHolderWrapper(@NonNull ViewHolderWrapper<T>... viewHolderWrapper) {

        return this;
    }



    @CheckResult
    public final <T> RegisterHelper<T> register(@NonNull Class<T> cls) {
        linkedViewTypeMap.remove(cls);
        return new RegisterHelper<T>(linkedViewTypeMap).register(cls);
    }

    @NonNull
    public LinkedViewTypeMap getLinkedViewTypeMap() {
        return linkedViewTypeMap;
    }

    public void setLinkedViewTypeMap(@NonNull LinkedViewTypeMap linkedViewTypeMap) {
        this.linkedViewTypeMap = linkedViewTypeMap;
    }

    @NonNull
    public List<?> getItems() {
        return items;
    }

    public void setItems(@NonNull List<?> items) {
        this.items = items;
    }
}
