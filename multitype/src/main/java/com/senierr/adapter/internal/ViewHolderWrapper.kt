package com.senierr.adapter.internal

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup

/**
 * 视图-数据封装
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */
abstract class ViewHolderWrapper<T>(@LayoutRes private val layoutId: Int = -1) {

    protected var recyclerView: RecyclerView? = null
    protected lateinit var multiTypeAdapter: MultiTypeAdapter

    private var onItemClickListener: ((viewHolder: ViewHolder, position: Int, item: T) -> Unit)? = null
    private var onItemLongClickListener: ((viewHolder: ViewHolder, position: Int, item: T) -> Boolean)? = null

    private var onChildClickListeners = SparseArray<(viewHolder: ViewHolder, view: View, position: Int, item: T) -> Unit>()
    private var onChildLongClickListeners = SparseArray<(viewHolder: ViewHolder, view: View, position: Int, item: T) -> Boolean>()

    /**
     * 注册回调
     */
    open fun onRegister(multiTypeAdapter: MultiTypeAdapter) {
        this.multiTypeAdapter = multiTypeAdapter
    }

    /**
     * 创建视图
     */
    internal fun onCreateViewHolderInternal(parent: ViewGroup): ViewHolder {
        val holder = onCreateViewHolder(parent)
        registerListeners(holder)
        return holder
    }

    /**
     * 创建视图
     */
    open fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder.create(parent, layoutId)
    }

    open fun onViewAttachedToWindow(holder: ViewHolder) {}

    open fun onViewDetachedFromWindow(holder: ViewHolder) {}

    open fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    open fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
    }

    abstract fun onBindViewHolder(holder: ViewHolder, item: T)

    open fun onBindViewHolder(holder: ViewHolder, item: T, payloads: List<Any>) {
        onBindViewHolder(holder, item)
    }

    /**
     * 获取当前项所占列数
     *
     * @param item 数据项
     * @return 默认为1，超过最大列数为全行
     */
    open fun getSpanSize(item: T): Int = 1

    @Suppress("UNCHECKED_CAST")
    private fun getItemData(position: Int): T? {
        var result: T? = null
        try {
            result = multiTypeAdapter.data[position] as T
        } catch (e: Exception) {
            Log.w(this.javaClass.simpleName, "The value in the position does not exist.")
        }
        return result
    }

    /**
     * 注册点击事件
     */
    private fun registerListeners(holder: ViewHolder) {
        // 绑定列表项点击事件
        onItemClickListener?.let { listener ->
            holder.itemView.setOnClickListener {
                val position = holder.layoutPosition
                val t = getItemData(position)
                if (t != null) listener.invoke(holder, position, t)
            }
        }

        // 绑定列表项长按事件
        onItemLongClickListener?.let { listener ->
            holder.itemView.setOnLongClickListener {
                val position = holder.layoutPosition
                val t = getItemData(position)
                t != null && listener.invoke(holder, position, t)
            }
        }

        // 绑定子视图点击事件
        if (onChildClickListeners.size() > 0) {
            for (i in 0 until onChildClickListeners.size()) {
                val key = onChildClickListeners.keyAt(i)
                val onChildClickListener = onChildClickListeners.get(key)
                val childView = holder.findView<View>(key)
                if (onChildClickListener != null) {
                    childView?.setOnClickListener { view ->
                        val position = holder.layoutPosition
                        val t = getItemData(position)
                        if (t != null) onChildClickListener.invoke(holder, view, position, t)
                    }
                }
            }
        }

        // 绑定子视图长按事件
        if (onChildLongClickListeners.size() > 0) {
            for (i in 0 until onChildLongClickListeners.size()) {
                val key = onChildLongClickListeners.keyAt(i)
                val onChildLongClickListener = onChildLongClickListeners.get(key)
                val childView = holder.findView<View>(key)
                if (onChildLongClickListener != null) {
                    childView?.setOnLongClickListener { view ->
                        val position = holder.layoutPosition
                        val t = getItemData(position)
                        t != null && onChildLongClickListener.invoke(holder, view, position, t)
                    }
                }
            }
        }
    }

    fun setOnItemClickListener(listener: (viewHolder: ViewHolder, position: Int, item: T) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: (viewHolder: ViewHolder, position: Int, item: T) -> Boolean) {
        onItemLongClickListener = listener
    }

    fun setOnChildClickListener(@IdRes childId: Int, listener: (viewHolder: ViewHolder, view: View, position: Int, item: T) -> Unit) {
        onChildClickListeners.put(childId, listener)
    }

    fun setOnChildLongClickListener(@IdRes childId: Int, listener: (viewHolder: ViewHolder, view: View, position: Int, item: T) -> Boolean) {
        onChildLongClickListeners.put(childId, listener)
    }
}