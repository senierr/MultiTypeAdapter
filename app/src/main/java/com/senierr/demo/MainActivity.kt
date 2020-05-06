package com.senierr.demo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.senierr.adapter.internal.Delegation
import com.senierr.adapter.internal.MultiTypeAdapter
import com.senierr.adapter.internal.ViewHolderWrapper
import com.senierr.demo.wrapper.FirstWrapper
import com.senierr.demo.wrapper.LoadMoreWrapper
import com.senierr.demo.wrapper.SecondWrapper
import com.senierr.demo.wrapper.StateWrapper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val multiTypeAdapter = MultiTypeAdapter()
    private val firstWrapper = FirstWrapper()
    private val secondWrapper = SecondWrapper()

    private var loadMoreWrapper = LoadMoreWrapper()
    private var stateWrapper = StateWrapper()

    private var pageIndex = 1
    private val pageSize = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        loadData()
    }

    /**
     * 初始化界面
     */
    private fun initView() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        rv_list.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        // 列表点击事件
        firstWrapper.setOnItemClickListener { _, _, item ->
            showToast("ItemClick: " + item.content)
        }
        firstWrapper.setOnItemLongClickListener { _, _, item ->
            showToast("ItemLongClick: " + item.content)
            true
        }
        // 子控件点击事件
        firstWrapper.setOnChildClickListener(R.id.btn_click) { _, _, _, dataBean ->
            showToast("ChildClick: " + dataBean.content)
        }
        firstWrapper.setOnChildLongClickListener(R.id.btn_click) { _, _, _, dataBean ->
            showToast("ChildLongClick: " + dataBean.content)
            true
        }
        // 加载更多
        loadMoreWrapper.onLoadMoreListener = {
            loadData()
        }
        // 状态切换
        stateWrapper.setOnItemClickListener { _, _, _ ->
            pageIndex = 1
            loadData()
        }

        multiTypeAdapter.register(firstWrapper, secondWrapper) {
            return@register if (it.id == 0) FirstWrapper::class.java else SecondWrapper::class.java
        }
        multiTypeAdapter.register(loadMoreWrapper)
        multiTypeAdapter.register(stateWrapper)

        rv_list.adapter = multiTypeAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_default) {
            stateWrapper.setState(StateWrapper.STATE_DEFAULT)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadData() {
        rv_list.postDelayed({
            val dataBeanList = DataBean.getData(pageIndex, pageSize)
            if (pageIndex == 1) {
                multiTypeAdapter.data.clear()
                multiTypeAdapter.data.addAll(dataBeanList)
                multiTypeAdapter.data.add(loadMoreWrapper.loadMoreBean)
                multiTypeAdapter.notifyDataSetChanged()
                pageIndex++
            } else if (pageIndex == 3) {
//                loadMoreWrapper.loadNoMore()
                loadMoreWrapper.loadFailure()
            } else {
                loadMoreWrapper.loadCompleted()
                val startPosition = multiTypeAdapter.data.size - 1
                multiTypeAdapter.data.addAll(startPosition, dataBeanList)
                multiTypeAdapter.notifyItemRangeInserted(startPosition, dataBeanList.size)
                pageIndex++
            }
        }, 1000)
    }

    private fun showToast(toastStr: String) {
        Toast.makeText(this, toastStr, Toast.LENGTH_SHORT).show()
    }
}
