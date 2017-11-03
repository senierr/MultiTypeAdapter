package com.senierr.seadapter.internal;

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

public class SeAdapter extends RecyclerView.Adapter<RVHolder> {

    private @Nullable RecyclerView recyclerView;
    private @NonNull List<Object> dataList;
    private @NonNull List<DataBinder<?>> dataBinderList;

    public SeAdapter() {
        this(new ArrayList<>());
    }

    public SeAdapter(@NonNull List<Object> dataList) {
        this.dataList = dataList;
        this.dataBinderList = new ArrayList<>();
    }

    @Override @SuppressWarnings("unchecked")
    public final int getItemViewType(int position) {
        Object item = dataList.get(position);
        return getViewHolderWrapperIndex(item);
    }

    @Override
    public final RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderWrapper<?> viewHolderWrapper = getViewHolderWrapper(viewType);
        return viewHolderWrapper.getViewHolder(parent);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onBindViewHolder(RVHolder holder, int position, List<Object> payloads) {
        Object item = dataList.get(position);
        ViewHolderWrapper<Object> viewHolderWrapper =
                (ViewHolderWrapper<Object>) getViewHolderWrapper(holder.getItemViewType());
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
        ViewHolderWrapper viewHolderWrapper = getViewHolderWrapper(getItemViewType(position));
        return viewHolderWrapper.getItemId(dataList.get(position));
    }

    @Override
    public final void onViewRecycled(RVHolder holder) {
        super.onViewRecycled(holder);
        ViewHolderWrapper viewHolderWrapper = getViewHolderWrapper(holder.getItemViewType());
        viewHolderWrapper.onViewRecycled(holder);
    }

    @Override
    public final boolean onFailedToRecycleView(RVHolder holder) {
        ViewHolderWrapper viewHolderWrapper = getViewHolderWrapper(holder.getItemViewType());
        return viewHolderWrapper.onFailedToRecycleView(holder);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onViewAttachedToWindow(RVHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (recyclerView == null) {
            return;
        }

        ViewHolderWrapper viewHolderWrapper = getViewHolderWrapper(holder.getItemViewType());
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
        ViewHolderWrapper viewHolderWrapper = getViewHolderWrapper(holder.getItemViewType());
        viewHolderWrapper.onViewDetachedFromWindow(holder);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;

        for (DataBinder<?> dataBinder : dataBinderList) {
            dataBinder.getViewHolderWrapper().onAttachedToRecyclerView(recyclerView);
        }

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    ViewHolderWrapper viewHolderWrapper = getViewHolderWrapper(getItemViewType(position));
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

        for (DataBinder<?> dataBinder : dataBinderList) {
            dataBinder.getViewHolderWrapper().onDetachedFromRecyclerView(recyclerView);
        }
    }

    /**
     * 绑定数据和封装器
     *
     * @param cls
     * @param viewHolderWrappers
     * @param <T>
     * @return
     */
    @NonNull @SafeVarargs @CheckResult
    public final <T> BindHelper<T> bind(@NonNull Class<T> cls,
                                        @NonNull ViewHolderWrapper<T>... viewHolderWrappers) {
        return new BindHelper<T>().bind(cls, viewHolderWrappers);
    }

    /**
     * 绑定数据和封装器
     *
     * @param cls
     * @param viewHolderWrapper
     * @param <T>
     */
    public final <T> void bind(@NonNull Class<T> cls,
                               @NonNull ViewHolderWrapper<T> viewHolderWrapper) {
        new BindHelper<T>().bind(cls, viewHolderWrapper).with(new DataBinder<T>() {
            @Override
            public int onBindIndex(@NonNull T item) {
                return 0;
            }
        });
    }

    /**
     * viewType -> index
     *
     * @param item
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> int getViewHolderWrapperIndex(T item) {
        int index = indexOf(item.getClass());
        return index + ((DataBinder<T>) dataBinderList.get(index)).onBindIndex(item);
    }

    /**
     * 获取封装器
     *
     * @return
     */
    public @NonNull List<ViewHolderWrapper<?>> getViewHolderWrappers() {
        List<ViewHolderWrapper<?>> viewHolderWrappers = new ArrayList<>();
        for (DataBinder<?> dataBinder : dataBinderList) {
            viewHolderWrappers.add(dataBinder.getViewHolderWrapper());
        }
        return viewHolderWrappers;
    }

    /**
     * 获取封装器
     *
     * @param index
     * @return
     */
    public ViewHolderWrapper getViewHolderWrapper(int index) {
        return dataBinderList.get(index).getViewHolderWrapper();
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
     * 查找对应Cls的DataBinder的索引
     *
     * @param cls
     * @return
     */
    private int indexOf(@NonNull Class cls) {
        for (int i = 0; i < dataBinderList.size(); i++) {
            if (cls.equals(dataBinderList.get(i).getDataCls())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 绑定辅助类
     *
     * @param <T>
     */
    public final class BindHelper<T> {

        private Class<T> cls;
        private ViewHolderWrapper<T>[] viewHolderWrappers;

        @NonNull @CheckResult @SafeVarargs
        final BindHelper<T> bind(@NonNull Class<T> cls, @NonNull ViewHolderWrapper<T>... viewHolderWrappers) {
            this.cls = cls;
            this.viewHolderWrappers = viewHolderWrappers;
            for (ViewHolderWrapper<?> viewHolderWrapper : viewHolderWrappers) {
                viewHolderWrapper.setAdapter(SeAdapter.this);
            }
            return this;
        }

        @SuppressWarnings("unchecked")
        public final void with(@NonNull DataBinder<T> dataBinder) {
            checkAndRemoveRepeats();
            dataBinder.setDataCls(cls);
            for (ViewHolderWrapper<T> viewHolderWrapper : viewHolderWrappers) {
                DataBinder<T> dataBinderTemp = null;
                try {
                    dataBinderTemp = (DataBinder<T>) dataBinder.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                if (dataBinderTemp != null) {
                    dataBinderTemp.setViewHolderWrapper(viewHolderWrapper);
                    dataBinderList.add(dataBinderTemp);
                }
            }
        }

        private void checkAndRemoveRepeats() {
            while (true) {
                int index = indexOf(cls);
                if (index != -1) {
                    dataBinderList.remove(index);
                } else {
                    break;
                }
            }
        }
    }
}
