package com.senierr.adapter.internal;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 多类型适配器
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */
public class MultiTypeAdapter extends RecyclerView.Adapter<ViewHolder> {

    private @Nullable RecyclerView recyclerView;
    private @NonNull List<Object> dataList;
    private @NonNull List<DataBinder<?>> dataBinderList;

    public MultiTypeAdapter() {
        this(new ArrayList<>());
    }

    public MultiTypeAdapter(@NonNull List<Object> dataList) {
        this.dataList = dataList;
        this.dataBinderList = new ArrayList<>();
    }

    @Override @SuppressWarnings("unchecked")
    public final int getItemViewType(int position) {
        Object item = dataList.get(position);
        return getViewHolderWrapperIndex(item);
    }

    @Override
    public final ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolderWrapper<?> viewHolderWrapper = getViewHolderWrapper(viewType);
        return viewHolderWrapper.generateViewHolder(parent, this);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onBindViewHolder(ViewHolder holder, int position, @NonNull List<Object> payloads) {
        Object item = dataList.get(position);
        ViewHolderWrapper<Object> viewHolderWrapper =
                (ViewHolderWrapper<Object>) getViewHolderWrapper(holder.getItemViewType());
        viewHolderWrapper.onBindViewHolder(holder, item, payloads);
    }

    @Override @Deprecated
    public final void onBindViewHolder(@NonNull ViewHolder holder, int position) {}

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
    public final void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        ViewHolderWrapper viewHolderWrapper = getViewHolderWrapper(holder.getItemViewType());
        viewHolderWrapper.onViewRecycled(holder);
    }

    @Override
    public final boolean onFailedToRecycleView(ViewHolder holder) {
        ViewHolderWrapper viewHolderWrapper = getViewHolderWrapper(holder.getItemViewType());
        return viewHolderWrapper.onFailedToRecycleView(holder);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onViewAttachedToWindow(@NonNull ViewHolder holder) {
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
            if (layoutManager == null) return;
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int position = holder.getLayoutPosition();
            int spanSize = viewHolderWrapper.getSpanSize(dataList.get(position));
            p.setFullSpan(spanSize >= staggeredGridLayoutManager.getSpanCount());
        }
    }

    @Override
    public final void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ViewHolderWrapper viewHolderWrapper = getViewHolderWrapper(holder.getItemViewType());
        viewHolderWrapper.onViewDetachedFromWindow(holder);
    }

    @Override @SuppressWarnings("unchecked")
    public final void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
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
    public final void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;

        for (DataBinder<?> dataBinder : dataBinderList) {
            dataBinder.getViewHolderWrapper().onDetachedFromRecyclerView(recyclerView);
        }
    }

    /**
     * viewType -> index
     *
     * @param item
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> int getViewHolderWrapperIndex(T item) {
        int index = indexOf(item.getClass());
        if (index < 0 || index > dataBinderList.size() - 1) {
            throw new IllegalArgumentException("Have you bound this type? : " + item.getClass().getSimpleName());
        }
        return index + ((DataBinder<T>) dataBinderList.get(index)).onBindIndex(item);
    }

    /**
     * 获取封装器
     *
     * @param index
     * @return
     */
    private ViewHolderWrapper getViewHolderWrapper(int index) {
        return dataBinderList.get(index).getViewHolderWrapper();
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

    /**************************** 数据管理 ********************************/
    public void addData(Object object) {
        dataList.add(object);
    }

    public void addData(int index, Object object) {
        dataList.add(index, object);
    }

    public void addDatas(Collection<?> c) {
        dataList.addAll(c);
    }

    public void addDatas(int index, Collection<?> c) {
        dataList.addAll(index, c);
    }

    public void removeData(Object object) {
        dataList.remove(object);
    }

    public void removeData(int index) {
        dataList.remove(index);
    }

    public void removeDatas(Collection<?> c) {
        dataList.removeAll(c);
    }

    public void clearData() {
        dataList.clear();
    }

    public int dataSize() {
        return dataList.size();
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T getData(int position) {
        try {
            return (T) dataList.get(position);
        } catch (Exception e) {
            return null;
        }
    }

    @NonNull
    public List<Object> getDataList() {
        return dataList;
    }

    public void setDataList(@NonNull List<Object> dataList) {
        this.dataList = dataList;
    }

    /**************************** 视图管理 ********************************/

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
                viewHolderWrapper.setMultiTypeAdapter(MultiTypeAdapter.this);
            }
            return this;
        }

        @SuppressWarnings("unchecked")
        public final void with(@NonNull DataBinder<T> dataBinder) {
            // 去除重复项
            while (true) {
                int index = indexOf(cls);
                if (index != -1) {
                    dataBinderList.remove(index);
                } else {
                    break;
                }
            }
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
    }
}
