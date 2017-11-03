package com.senierr.adapter.support.wrapper;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.senierr.adapter.core.ViewHolderWrapper;
import com.senierr.adapter.support.bean.StateBean;

/**
 * 状态切换
 *
 * @author zhouchunjie
 * @date 2017/10/9
 */

public abstract class BaseStateWrapper extends ViewHolderWrapper<StateBean> {

    private @Nullable RecyclerView recyclerView;
    private @NonNull StateBean stateBean;

    public BaseStateWrapper() {
        stateBean = new StateBean();
        stateBean.setState(StateBean.STATE_NONE);
    }

    @Override
    public int getSpanSize(StateBean item) {
        if (recyclerView == null) {
            return super.getSpanSize(item);
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    /**
     * 更新布局
     */
    private void refreshView(int state) {
        stateBean.setState(state);
        if (getMultiTypeAdapter() != null && recyclerView != null) {
            getMultiTypeAdapter().getDataList().clear();
            getMultiTypeAdapter().getDataList().add(stateBean);
            getMultiTypeAdapter().notifyDataSetChanged();
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                ((StaggeredGridLayoutManager) layoutManager).invalidateSpanAssignments();
            }
            layoutManager.scrollToPosition(0);
        }
    }

    /**
     * 隐藏状态布局
     */
    public void hide() {
        if (getMultiTypeAdapter() != null) {
            int stateBeanIndex = getMultiTypeAdapter().getDataList().indexOf(stateBean);
            if (stateBeanIndex != -1) {
                getMultiTypeAdapter().getDataList().remove(stateBeanIndex);
                getMultiTypeAdapter().notifyDataSetChanged();
            }
        }
    }

    /**
     * 显示自定义状态
     */
    public final void show(@IntRange(from = 0, to = Integer.MAX_VALUE) int state) {
        if (state < 0) {
            throw new IllegalArgumentException("The state must be greater than 0");
        }
        refreshView(state);
    }

    /**
     * 加载中
     */
    public final void showLoading() {
        refreshView(StateBean.STATE_LOADING);
    }

    /**
     * 空数据
     */
    public final void showEmpty() {
        refreshView(StateBean.STATE_EMPTY);
    }

    /**
     * 错误数据
     */
    public final void showError() {
        refreshView(StateBean.STATE_ERROR);
    }

    /**
     * 没网络
     */
    public final void showNoNetwork() {
        refreshView(StateBean.STATE_NO_NETWORK);
    }
}
