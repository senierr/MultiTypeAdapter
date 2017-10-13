package com.senierr.adapter.support;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.senierr.adapter.ViewHolderWrapper;
import com.senierr.adapter.util.RecyclerViewUtil;

/**
 * 状态切换
 *
 * @author zhouchunjie
 * @date 2017/10/9
 */

public abstract class BaseStateWrapper extends ViewHolderWrapper<BaseStateWrapper.StateBean> {

    private @Nullable RecyclerView recyclerView;
    private @NonNull StateBean stateBean;

    public BaseStateWrapper(@LayoutRes int layoutId) {
        super(StateBean.class, layoutId);
        stateBean = new StateBean();
        loadMoreBean.setLoadState(LoadMoreBean.STATUS_LOADING_COMPLETED);
    }

    @Override
    public int getSpanSize(StateBean item) {
        if (recyclerView == null) {
            return super.getSpanSize(item);
        }
        return RecyclerViewUtil.getSpanCount(recyclerView);
    }

    /**
     * 更新加载更多布局
     */
    private void refreshStateView() {
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

    public class StateBean {

        public final static int STATUS_NO = 101;
        public final static int STATUS_EMPTY = 102;
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
