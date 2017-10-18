package com.senierr.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.senierr.adapter.core.MultiTypeAdapter;
import com.senierr.adapter.core.RVHolder;
import com.senierr.adapter.core.ViewHolderWrapper;
import com.senierr.adapter.support.BaseLoadMoreWrapper;
import com.senierr.adapter.support.RVItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toast toast;
    private RecyclerView recyclerView;
    private List<Object> list = new ArrayList<>();
    private MultiTypeAdapter multiTypeAdapter;

    private LoadMoreWrapper loadMoreWrapper;
    private StateWrapper stateWrapper;

    private int pageIndex = 1;
    private int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        loadData();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new RVItemDecoration(this, R.dimen.dimen_4, R.color.transparent));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        multiTypeAdapter = new MultiTypeAdapter();

        FirstWrapper firstWrapper = new FirstWrapper();
        // 列表点击事件
        firstWrapper.setOnItemClickListener(new ViewHolderWrapper.OnItemClickListener() {
            @Override
            public void onClick(RVHolder viewHolder, int position) {
                showToast("ItemClick: " + position);
            }

            @Override
            public boolean onLongClick(RVHolder viewHolder, int position) {
                showToast("ItemLongClick: " + position);
                return true;
            }
        });

        // 子控件点击事件
        firstWrapper.setOnItemChildClickListener(R.id.btn_click,
                new ViewHolderWrapper.OnItemChildClickListener() {
            @Override
            public void onClick(RVHolder viewHolder, View view, int position) {
                showToast("ChildClick: " + position);
            }

            @Override
            public boolean onLongClick(RVHolder viewHolder, View view, int position) {
                showToast("ChildLongClick: " + position);
                return true;
            }
        });

        // 加载更多
        loadMoreWrapper = new LoadMoreWrapper();
        loadMoreWrapper.setOnLoadMoreListener(new BaseLoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadData();
            }
        });
        // 状态切换
        stateWrapper = new StateWrapper();

        multiTypeAdapter.register(firstWrapper, new SecondWrapper(),
                loadMoreWrapper, stateWrapper);
        multiTypeAdapter.setDataList(list);
        recyclerView.setAdapter(multiTypeAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_loading) {
            stateWrapper.showLoading();
            pageIndex = 1;
            loadData();
        } else if (id == R.id.action_empty) {
            stateWrapper.showEmpty();
        } else if (id == R.id.action_error) {
            stateWrapper.showError();
        } else if (id == R.id.action_no_network) {
            stateWrapper.showNoNetwork();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<DataBean> dataBeanList = DataBean.getData(pageIndex, pageSize);
                if (pageIndex == 1) {
                    list.clear();
                    list.addAll(dataBeanList);
                    list.add(loadMoreWrapper.getLoadMoreBean());
                    multiTypeAdapter.notifyDataSetChanged();
                    pageIndex++;
                } else if (pageIndex == 3) {
                    loadMoreWrapper.loadMoreNoMore();
//                        loadMoreWrapper.loadMoreFailure();
                } else {
                    loadMoreWrapper.loadMoreCompleted();
                    int startPosition = list.size() - 1;
                    list.addAll(startPosition, dataBeanList);
                    multiTypeAdapter.notifyItemRangeInserted(startPosition, dataBeanList.size());
                    pageIndex++;
                }
            }
        }, 1000);
    }

    /**
     * 信息提示
     *
     * @param toastStr
     */
    protected void showToast(String toastStr) {
        if (toast == null) {
            toast = Toast.makeText(this, toastStr, Toast.LENGTH_SHORT);
        }
        toast.setText(toastStr);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
