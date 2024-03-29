package com.senierr.demo.wrapper

import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.senierr.adapter.internal.ViewHolder
import com.senierr.adapter.internal.ViewHolderWrapper
import com.senierr.demo.R
import com.senierr.demo.entity.Data2

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */
class Data2Wrapper : ViewHolderWrapper<Data2>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val rvHolder = ViewHolder.create(parent, R.layout.item_second)
        val checkBox = rvHolder.findView<CheckBox>(R.id.cb_check)
        checkBox?.setOnCheckedChangeListener { v, b ->
            Toast.makeText(v.context, rvHolder.layoutPosition.toString() + ", Check: " + b, Toast.LENGTH_SHORT).show()
        }
        return rvHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Data2) {
        val textView = holder.findView<TextView>(R.id.tv_text)
        textView?.text = item.content
        textView?.height = item.height
        val checkBox = holder.findView<CheckBox>(R.id.cb_check)
        checkBox?.isChecked = false
    }
}