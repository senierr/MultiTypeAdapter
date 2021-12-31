package com.senierr.demo.wrapper

import android.widget.TextView
import com.senierr.adapter.internal.ViewHolder
import com.senierr.adapter.internal.ViewHolderWrapper
import com.senierr.demo.R
import com.senierr.demo.entity.Data1

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */
class Data1Wrapper : ViewHolderWrapper<Data1>(R.layout.item_first) {

    override fun onBindViewHolder(holder: ViewHolder, item: Data1) {
        val textView = holder.findView<TextView>(R.id.tv_text)
        textView?.text = item.content
        textView?.height = item.height
    }
}