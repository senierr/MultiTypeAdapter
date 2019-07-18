package com.senierr.demo.wrapper

import android.widget.TextView
import com.senierr.adapter.internal.ViewHolder
import com.senierr.adapter.internal.ViewHolderWrapper
import com.senierr.demo.DataBean
import com.senierr.demo.R

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */
class FirstWrapper : ViewHolderWrapper<DataBean>(R.layout.item_first) {

    override fun onBindViewHolder(holder: ViewHolder, item: DataBean) {
        val textView = holder.findView<TextView>(R.id.tv_text)
        textView?.text = item.content
        textView?.height = item.height
    }
}