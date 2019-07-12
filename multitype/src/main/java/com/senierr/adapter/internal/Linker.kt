package com.senierr.adapter.internal

/**
 * 数据-视图连接器
 *
 * 用于描述数据类型所对应的视图类型
 *
 * 封装器-Key: 一个封装器可以处理多种数据类型
 * 处理的数据类型-Value
 *
 * @author zhouchunjie
 * @date 2018/11/5
 */
class Linker {

    /**
     * 委托: 数据类型委托给特定的封装器
     */
    interface Delegation<T> {
        fun getWrapperType(item: T): Class<out ViewHolderWrapper<T>>
    }

    val wrappers = mutableListOf<ViewHolderWrapper<*>>()
    val types = mutableListOf<Class<*>>()
    val delegations = mutableListOf<Delegation<*>>()

    inline fun <reified T> register(viewHolderWrapper: ViewHolderWrapper<T>): Linker {
        val linker = Linker()
        linker.wrappers.add(viewHolderWrapper)
        linker.types.add(T::class.java)
        linker.delegations.add(object : Delegation<T> {
            override fun getWrapperType(item: T): Class<out ViewHolderWrapper<T>> {
                return viewHolderWrapper::class.java
            }
        })
        return linker
    }

    inline fun <reified T> register(viewHolderWrapper: ViewHolderWrapper<T>, delegation: Delegation<T>): Linker {
        val linker = Linker()
        linker.wrappers.add(viewHolderWrapper)
        linker.types.add(T::class.java)
        linker.delegations.add(delegation)
        return linker
    }

    private fun checkIfRegister(viewHolderWrapper: ViewHolderWrapper<*>): Boolean {
        wrappers.forEach {
            if (it::class.java == viewHolderWrapper::class.java) {

            }
        }
    }
}