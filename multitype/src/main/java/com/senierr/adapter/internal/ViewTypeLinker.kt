package com.senierr.adapter.internal

/**
 *
 * @author senierr_zhou
 * @date 2021/12/31
 */
interface ViewTypeLinker {
    fun getItemViewType(item: Any): Int

    fun getViewHolderWrapper(itemViewType: Int): ViewHolderWrapper<*>
}