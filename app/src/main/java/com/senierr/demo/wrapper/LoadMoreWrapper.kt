package com.senierr.demo.wrapper

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.senierr.adapter.internal.ViewHolder
import com.senierr.adapter.support.bean.LoadMoreBean
import com.senierr.adapter.support.wrapper.BaseLoadMoreWrapper
import com.senierr.demo.R

/**
 * @author zhouchunjie
 * @date 2017/10/11
 */
class LoadMoreWrapper : BaseLoadMoreWrapper(R.layout.item_load_more) {

    override fun onBindViewHolder(holder: ViewHolder, item: LoadMoreBean) {
        val textView = holder.findView<TextView>(R.id.tv_text)
        val progressBar = holder.findView<ProgressBar>(R.id.pb_bar)
        when (item.loadState) {
            LoadMoreBean.STATUS_LOADING -> {
                textView?.setText(R.string.loading)
                progressBar?.visibility = View.VISIBLE
            }
            LoadMoreBean.STATUS_COMPLETED -> {
                textView?.setText(R.string.completed)
                progressBar?.visibility = View.GONE
            }
            LoadMoreBean.STATUS_NO_MORE -> {
                textView?.setText(R.string.no_more)
                progressBar?.visibility = View.GONE
            }
            LoadMoreBean.STATUS_FAILURE -> {
                textView?.setText(R.string.failure)
                progressBar?.visibility = View.GONE
            }
        }
    }
}
