package com.senierr.adapter.support.wrapper

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.senierr.adapter.internal.ViewHolderWrapper
import com.senierr.adapter.support.bean.StateBean

/**
 * 状态封装
 *
 * @author zhouchunjie
 * @date 2017/10/9
 */
abstract class BaseStateWrapper(@LayoutRes private val layoutId: Int = -1) : ViewHolderWrapper<StateBean>(layoutId) {

    private var recyclerView: RecyclerView? = null
    private val stateBean = StateBean()

    override fun getSpanSize(item: StateBean): Int = Integer.MAX_VALUE

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    /**
     * 更新状态
     *
     * @param state
     */
    fun setState(state: Int) {
        stateBean.state = state
        recyclerView?.let {
            multiTypeAdapter.data.clear()
            multiTypeAdapter.data.add(stateBean)
            multiTypeAdapter.notifyDataSetChanged()
            val layoutManager = it.layoutManager
            if (layoutManager != null) {
                if (layoutManager is StaggeredGridLayoutManager) {
                    layoutManager.invalidateSpanAssignments()
                }
                layoutManager.scrollToPosition(0)
            }
        }
    }
}
