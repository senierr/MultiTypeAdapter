package com.senierr.adapter.internal

/**
 * 委托
 *
 * 数据类型委托给封装器的描述
 *
 * @author zhouchunjie
 * @date 2019/7/17
 */
interface Delegation<T> {
    fun getWrapperType(item: T): Class<out ViewHolderWrapper<T>>
}