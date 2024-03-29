package com.senierr.adapter.internal

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.ViewGroup

/**
 * 多类型适配器
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */
class MultiTypeAdapter(var data: MutableList<Any> = mutableListOf()) : RecyclerView.Adapter<ViewHolder>() {

    private var recyclerView: RecyclerView? = null
    private val itemBinders = mutableListOf<ItemBinder<*>>()
    private val wrapperCache = mutableListOf<ViewHolderWrapper<*>>()
    // 自定义类型匹配
    private var viewTypeLinker: ViewTypeLinker? = null

    override fun getItemViewType(position: Int): Int {
        val item = data[position]

        // 优先自定义视图类型
        val linker = viewTypeLinker
        if (linker != null) {
            return linker.getItemViewType(item)
        }

        val index = indexOf(item)
        if (index == -1) throw UnregisteredException(item.javaClass)
        return index
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolderWrapper = getViewHolderWrapper<Any>(viewType)
        return viewHolderWrapper.onCreateViewHolderInternal(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: List<Any>) {
        val item = data[position]
        val viewHolderWrapper = getViewHolderWrapper<Any>(holder.itemViewType)
        viewHolderWrapper.onBindViewHolder(holder, item, payloads)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = data.size

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val viewHolderWrapper = getViewHolderWrapper<Any>(holder.itemViewType)
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
        val viewHolderWrapper = getViewHolderWrapper<Any>(holder.itemViewType)
        viewHolderWrapper.onViewDetachedFromWindow(holder)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView

        val layoutManager = recyclerView.layoutManager
        if (layoutManager != null && layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewHolderWrapper = getViewHolderWrapper<Any>(getItemViewType(position))
                    val spanSize = viewHolderWrapper.getSpanSize(data[position])
                    val spanCount = layoutManager.spanCount
                    return if (spanSize > spanCount) spanCount else spanSize
                }
            }
        }

        for (viewHolderWrapper in wrapperCache) {
            viewHolderWrapper.onAttachedToRecyclerView(recyclerView)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null

        for (viewHolderWrapper in wrapperCache) {
            viewHolderWrapper.onDetachedFromRecyclerView(recyclerView)
        }
    }

    /**
     * 获取数据项对应的封装器的索引
     */
    @Suppress("UNCHECKED_CAST")
    private fun indexOf(item: Any): Int {
        var index = 0
        itemBinders.forEach {
            if (it.type == item::class.java) {
                val delegation = it.delegation as Delegation<Any>
                val wrapperType = delegation.getWrapperType(item)
                for (i in 0 until it.viewHolderWrappers.size) {
                    if (it.viewHolderWrappers[i]::class.java == wrapperType) {
                        index += i
                        return index
                    }
                }
            } else {
                index += it.viewHolderWrappers.size
            }
        }
        return -1
    }

    /**
     * 获取封装器
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> getViewHolderWrapper(itemViewType: Int): ViewHolderWrapper<T> {
        // 优先自定义视图类型
        val linker = viewTypeLinker
        if (linker != null) {
            return linker.getViewHolderWrapper(itemViewType) as ViewHolderWrapper<T>
        }
        return wrapperCache[itemViewType] as ViewHolderWrapper<T>
    }

    /**
     * 重置封装器
     */
    private fun resetWrapperCache() {
        wrapperCache.clear()
        itemBinders.forEach {
            wrapperCache.addAll(it.viewHolderWrappers)
        }
    }

    /**
     * 解除注册
     */
    fun <T> unregister(type: Class<T>) {
        val iterator = itemBinders.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.type == type) {
                iterator.remove()
            }
        }
        resetWrapperCache()
    }

    /**
     * 注册
     */
    fun <T> register(type: Class<T>, vararg viewHolderWrapper: ViewHolderWrapper<T>, delegation: Delegation<T>) {
        // 解除相同数据类型的注册
        unregister(type)
        // 注册
        itemBinders.add(ItemBinder(type, mutableListOf(*viewHolderWrapper), delegation))
        viewHolderWrapper.forEach {
            it.onRegister(this)
        }
        // 重置缓存
        resetWrapperCache()
    }

    /**
     * 注册
     */
    fun <T> register(type: Class<T>, viewHolderWrapper: ViewHolderWrapper<T>) {
        register(type, viewHolderWrapper, delegation = object : Delegation<T> {
            override fun getWrapperType(item: T): Class<out ViewHolderWrapper<T>> {
                return viewHolderWrapper::class.java
            }
        })
    }

    /**
     * 注册自定义匹配规则
     */
    fun register(viewHolderWrapper: List<ViewHolderWrapper<*>>, viewTypeLinker: ViewTypeLinker) {
        wrapperCache.clear()
        wrapperCache.addAll(viewHolderWrapper)
        wrapperCache.forEach {
            it.onRegister(this)
        }
        this.viewTypeLinker = viewTypeLinker
    }

    inline fun <reified T> register(viewHolderWrapper: ViewHolderWrapper<T>) {
        register(T::class.java, viewHolderWrapper)
    }

    inline fun <reified T> register(vararg viewHolderWrapper: ViewHolderWrapper<T>, delegation: Delegation<T>) {
        register(T::class.java, *viewHolderWrapper, delegation = delegation)
    }

    inline fun <reified T> register(vararg viewHolderWrapper: ViewHolderWrapper<T>,
                                    crossinline delegation: (item: T) -> Class<out ViewHolderWrapper<T>>) {
        register(T::class.java, *viewHolderWrapper, delegation = object : Delegation<T> {
            override fun getWrapperType(item: T): Class<out ViewHolderWrapper<T>> {
                return delegation.invoke(item)
            }
        })
    }
}