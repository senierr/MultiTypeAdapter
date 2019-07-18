package com.senierr.demo.wrapper

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.senierr.adapter.internal.ViewHolder
import com.senierr.adapter.support.bean.StateBean
import com.senierr.adapter.support.wrapper.BaseStateWrapper
import com.senierr.demo.R

/**
 * @author zhouchunjie
 * @date 2017/10/13
 */
class StateWrapper : BaseStateWrapper(R.layout.item_state) {

    override fun onBindViewHolder(holder: ViewHolder, item: StateBean) {
        val textView = holder.findView<TextView>(R.id.tv_text)
        val progressBar = holder.findView<ProgressBar>(R.id.pb_bar)
        when (item.state) {
            STATE_DEFAULT -> {
                textView?.setText(R.string.state_default)
                progressBar?.visibility = View.GONE
            }
        }
    }

    companion object {
        const val STATE_DEFAULT = 0
    }
}
