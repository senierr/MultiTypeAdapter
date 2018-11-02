package com.senierr.adapter.support.wrapper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.senierr.adapter.internal.MultiTypeAdapter;
import com.senierr.adapter.internal.ViewHolderWrapper;
import com.senierr.adapter.support.bean.StateBean;

/**
 * 状态封装
 *
 * @author zhouchunjie
 * @date 2017/10/9
 */
public abstract class BaseStateWrapper extends ViewHolderWrapper<StateBean> {

    private @Nullable RecyclerView recyclerView;
    private @NonNull StateBean stateBean;

    public BaseStateWrapper() {
        stateBean = new StateBean();
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
     * 更新状态
     *
     * @param state
     */
    public void setState(int state) {
        stateBean.setState(state);
        MultiTypeAdapter multiTypeAdapter = getMultiTypeAdapter();
        if (multiTypeAdapter != null && recyclerView != null) {
            multiTypeAdapter.getDataList().clear();
            multiTypeAdapter.getDataList().add(stateBean);
            multiTypeAdapter.notifyDataSetChanged();
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager != null) {
                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    ((StaggeredGridLayoutManager) layoutManager).invalidateSpanAssignments();
                }
                layoutManager.scrollToPosition(0);
            }
        }
    }
}
