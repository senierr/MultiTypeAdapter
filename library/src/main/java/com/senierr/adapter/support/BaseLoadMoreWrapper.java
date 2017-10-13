package com.senierr.adapter.support;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.senierr.adapter.ViewHolderWrapper;
import com.senierr.adapter.util.RecyclerViewUtil;

/**
 * 加载更多
 *
 * @author zhouchunjie
 * @date 2017/10/9
 */

public abstract class BaseLoadMoreWrapper extends ViewHolderWrapper<BaseLoadMoreWrapper.LoadMoreBean> {

    private @Nullable RecyclerView recyclerView;
    private @NonNull LoadMoreBean loadMoreBean;
    private @Nullable OnLoadMoreListener onLoadMoreListener;

    public BaseLoadMoreWrapper(@LayoutRes int layoutId) {
        super(LoadMoreBean.class, layoutId);
        loadMoreBean = new LoadMoreBean();
        loadMoreBean.setLoadState(LoadMoreBean.STATUS_LOADING_COMPLETED);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (checkCanLoadMore(recyclerView, dx, dy)) {
                    loadMoreStart();
                }
            }
        });
    }

    @Override
    public int getSpanSize(LoadMoreBean item) {
        if (recyclerView == null) {
            return super.getSpanSize(item);
        }
        return RecyclerViewUtil.getSpanCount(recyclerView);
    }

    /**
     * 判断是否可以加载更多
     *
     * @param recyclerView
     */
    private boolean checkCanLoadMore(RecyclerView recyclerView, int dx, int dy) {
        // 判断是否有加载更多项
        boolean hasLoadMoreBean = false;
        if (multiTypeAdapter != null) {
            hasLoadMoreBean = multiTypeAdapter.getDataList().indexOf(loadMoreBean) != -1;
        }
        if (hasLoadMoreBean && loadMoreBean.getLoadState() != LoadMoreBean.STATUS_LOADING &&
                RecyclerViewUtil.getLastVisibleItemPosition(recyclerView) + 1 >= recyclerView.getAdapter().getItemCount()) {
            int orientation = RecyclerViewUtil.getOrientation(recyclerView);
            if (orientation == OrientationHelper.VERTICAL && dy > 0) {
                return true;
            } else if (orientation == OrientationHelper.HORIZONTAL && dx > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 更新加载更多布局
     */
    private void refreshLoadMoreView() {
        if (multiTypeAdapter != null) {
            multiTypeAdapter.notifyItemChanged(multiTypeAdapter.getItemCount() - 1);
        }
    }

    /**
     * 开始加载
     */
    public final void loadMoreStart() {
        loadMoreBean.setLoadState(LoadMoreBean.STATUS_LOADING);
        if (onLoadMoreListener != null && recyclerView != null) {
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    refreshLoadMoreView();
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                }
            });
        }
    }

    /**
     * 加载完成
     */
    public final void loadMoreCompleted() {
        loadMoreBean.setLoadState(LoadMoreBean.STATUS_LOADING_COMPLETED);
        refreshLoadMoreView();
    }

    /**
     * 没有更多数据
     */
    public final void loadMoreNoMore() {
        loadMoreBean.setLoadState(LoadMoreBean.STATUS_LOAD_NO_MORE);
        refreshLoadMoreView();
    }

    /**
     * 加载失败
     */
    public final void loadMoreFailure() {
        loadMoreBean.setLoadState(LoadMoreBean.STATUS_LOAD_FAILURE);
        refreshLoadMoreView();
    }

    /**
     * 是否正在加载
     *
     * @return
     */
    public final boolean isLoading() {
        return loadMoreBean.getLoadState() == LoadMoreBean.STATUS_LOADING;
    }

    @Nullable
    public final OnLoadMoreListener getOnLoadMoreListener() {
        return onLoadMoreListener;
    }

    public final void setOnLoadMoreListener(@Nullable OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @NonNull
    public final LoadMoreBean getLoadMoreBean() {
        return loadMoreBean;
    }

    public final void setLoadMoreBean(@NonNull LoadMoreBean loadMoreBean) {
        this.loadMoreBean = loadMoreBean;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public class LoadMoreBean {

        public final static int STATUS_LOADING = 101;
        public final static int STATUS_LOADING_COMPLETED = 102;
        public final static int STATUS_LOAD_NO_MORE = 103;
        public final static int STATUS_LOAD_FAILURE = 104;

        private int loadState;

        public int getLoadState() {
            return loadState;
        }

        public void setLoadState(int loadState) {
            this.loadState = loadState;
        }
    }
}
