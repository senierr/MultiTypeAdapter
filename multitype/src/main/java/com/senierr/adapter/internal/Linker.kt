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
data class Linker<T>(
        val type: Class<T>,
        val viewHolderWrappers: MutableList<ViewHolderWrapper<T>>,
        val delegation: Delegation<T>
)