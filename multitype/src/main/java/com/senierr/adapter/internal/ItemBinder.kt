package com.senierr.adapter.internal

/**
 * 数据-视图绑定器
 *
 * @author zhouchunjie
 * @date 2018/11/5
 */
data class ItemBinder<T>(
        val type: Class<T>, // 待处理的数据类型
        val viewHolderWrappers: MutableList<ViewHolderWrapper<T>>,  // 可供选择的封装器
        val delegation: Delegation<T>   // 委托方式
)