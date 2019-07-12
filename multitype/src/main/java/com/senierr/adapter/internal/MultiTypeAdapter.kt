package com.senierr.adapter.internal

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.ViewGroup
import java.util.*

/**
 * 多类型适配器
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */
class MultiTypeAdapter(var data: MutableList<Any> = mutableListOf()) : RecyclerView.Adapter<ViewHolder>() {

    private var recyclerView: RecyclerView? = null
    // 视图集合：对应的Index即为ItemViewType
    private val wrappers = mutableListOf<ViewHolderWrapper<*>>()
    private val registerForm = mutableMapOf<Class<*>, >()

    override fun getItemViewType(position: Int): Int {

        val item = data[position]
        for (index in 0 until wrapperList.size) {
            val wrapper = wrapperList[index]
            // 所属类型并且同意处理
            if (item.javaClass == wrapper.type && wrapper.interceptData(item)) {
                return index
            }
        }
        throw UnregisteredException(item.javaClass)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolderWrapper = wrapperList[viewType]
        return viewHolderWrapper.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {
        val item = data[position]
        val viewHolderWrapper = wrapperList[holder.itemViewType]
        viewHolderWrapper.onBindViewHolder(holder, item, payloads)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = data.size

    override fun getItemId(position: Int): Long {
        val viewHolderWrapper = wrapperList[getItemViewType(position)]
        return getViewHolder<Any>(position).getItemId(data[position])
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        val viewHolderWrapper = wrapperList[holder.itemViewType]
        viewHolderWrapper.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: ViewHolder): Boolean {
        val viewHolderWrapper = wrapperList[holder.itemViewType]
        return viewHolderWrapper.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val viewHolderWrapper = wrapperList[holder.itemViewType]
        viewHolderWrapper.onViewAttachedToWindow(holder)

        recyclerView?.let {
            val lp = holder.itemView.layoutParams
            val layoutManager = it.layoutManager
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams
                    && layoutManager != null && layoutManager is StaggeredGridLayoutManager) {
                val position = holder.layoutPosition
                val spanSize = viewHolderWrapper.getSpanSize(data[position])
                lp.isFullSpan = spanSize >= layoutManager.spanCount
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val viewHolderWrapper = wrapperList[holder.itemViewType]
        viewHolderWrapper.onViewDetachedFromWindow(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView

        val layoutManager = recyclerView.layoutManager
        if (layoutManager != null && layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewHolderWrapper = wrapperList[getItemViewType(position)]
                    val spanSize = viewHolderWrapper.getSpanSize(data[position])
                    val spanCount = layoutManager.spanCount
                    return if (spanSize > spanCount) spanCount else spanSize
                }
            }
        }

        for (viewHolderWrapper in wrapperList) {
            viewHolderWrapper.onAttachedToRecyclerView(recyclerView)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null

        for (viewHolderWrapper in wrapperList) {
            viewHolderWrapper.onDetachedFromRecyclerView(recyclerView)
        }
    }

    /** Internal *****************************************************************************************/

    @Suppress("UNCHECKED_CAST")
    fun <T> getViewHolderWrapper(itemViewType: Int): ViewHolderWrapper<T> {
        return wrapperList[itemViewType] as ViewHolderWrapper<T>
    }

    fun <T> register(viewHolderWrapper: ViewHolderWrapper<T>) {
        wrapperList.add(viewHolderWrapper)
        viewHolderWrapper.onRegister(this)
    }
}