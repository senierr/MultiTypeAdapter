package com.senierr.demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.senierr.adapter.internal.MultiTypeAdapter
import com.senierr.adapter.internal.ViewHolderWrapper
import com.senierr.adapter.internal.ViewTypeLinker
import com.senierr.adapter.support.bean.LoadMoreBean
import com.senierr.demo.databinding.ActivityMainBinding
import com.senierr.demo.entity.Data1
import com.senierr.demo.entity.Data2
import com.senierr.demo.entity.IData
import com.senierr.demo.wrapper.Data1Wrapper
import com.senierr.demo.wrapper.Data2Wrapper
import com.senierr.demo.wrapper.LoadMoreWrapper
import com.senierr.demo.wrapper.StateWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val multiTypeAdapter = MultiTypeAdapter()

    private val data1Wrapper = Data1Wrapper()
    private val data2Wrapper = Data2Wrapper()

    private var loadMoreWrapper = LoadMoreWrapper()
    private var stateWrapper = StateWrapper()

    private var pageIndex = 0
    private val pageSize = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        loadData()
    }

    /**
     * 初始化界面
     */
    private fun initView() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.rvList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // 列表点击事件
        data1Wrapper.setOnItemClickListener { _, _, item ->
            showToast("ItemClick: " + item.content)
        }
        data1Wrapper.setOnItemLongClickListener { _, _, item ->
            showToast("ItemLongClick: " + item.content)
            true
        }
        // 子控件点击事件
        data1Wrapper.setOnChildClickListener(R.id.btn_click) { _, _, _, dataBean ->
            showToast("ChildClick: " + dataBean.content)
        }
        data1Wrapper.setOnChildLongClickListener(R.id.btn_click) { _, _, _, dataBean ->
            showToast("ChildLongClick: " + dataBean.content)
            true
        }
        // 加载更多
        loadMoreWrapper.onLoadMoreListener = {
            loadData()
        }
        // 状态切换
        stateWrapper.setOnItemClickListener { _, _, _ ->
            loadData(true)
        }

        multiTypeAdapter.register(
            listOf(data1Wrapper, data2Wrapper, loadMoreWrapper, stateWrapper),
            object : ViewTypeLinker {
                override fun getItemViewType(item: Any): Int {
                    if (item is IData && item.getType() == 1) {
                        return 1
                    } else if (item is IData && item.getType() == 2) {
                        return 2
                    } else if (item is LoadMoreBean) {
                        return 3
                    } else {
                        return 0
                    }
                }

                override fun getViewHolderWrapper(itemViewType: Int): ViewHolderWrapper<*> {
                    return when (itemViewType) {
                        0 -> { stateWrapper }
                        1 -> { data1Wrapper }
                        2 -> { data2Wrapper }
                        3 -> { loadMoreWrapper }
                        else -> {
                            throw IllegalStateException("")
                        }
                    }
                }
            })

        binding.rvList.adapter = multiTypeAdapter
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

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData(isRefresh: Boolean = false) {
        if (isRefresh) pageIndex = 0
        lifecycleScope.launch {
            delay(2000)
            val result = fetchData(pageIndex)
            if (pageIndex == 0) {
                multiTypeAdapter.data.clear()
                multiTypeAdapter.data.addAll(result)
                multiTypeAdapter.data.add(loadMoreWrapper.loadMoreBean)
                multiTypeAdapter.notifyDataSetChanged()
                pageIndex++
            } else if (pageIndex > 3) {
                if ((System.currentTimeMillis() % 2) == 0L) {
                    loadMoreWrapper.loadFailure()
                } else {
                    loadMoreWrapper.loadNoMore()
                }
            } else {
                loadMoreWrapper.loadCompleted()
                val startPosition = multiTypeAdapter.data.size - 1
                multiTypeAdapter.data.addAll(startPosition, result)
                multiTypeAdapter.notifyItemRangeInserted(startPosition, result.size)
                pageIndex++
            }
        }
    }

    private fun fetchData(pageIndex: Int): List<IData> {
        val result1 = mutableListOf<Data1>()
        for (i in 0 until pageSize / 2) {
            result1.add(Data1("$pageIndex-$i", Random().nextInt(300) + 100))
        }
        val result2 = mutableListOf<Data2>()
        for (i in 0 until pageSize / 2) {
            result2.add(Data2("$pageIndex-$i", Random().nextInt(300) + 100))
        }
        return result1 + result2
    }

    private fun showToast(toastStr: String) {
        Toast.makeText(this, toastStr, Toast.LENGTH_SHORT).show()
    }
}
