package com.senierr.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.senierr.adapter.internal.DataBinder;
import com.senierr.adapter.internal.MultiTypeAdapter;
import com.senierr.adapter.internal.ViewHolder;
import com.senierr.adapter.listener.OnChildClickListener;
import com.senierr.adapter.listener.OnChildLongClickListener;
import com.senierr.adapter.listener.OnItemClickListener;
import com.senierr.adapter.listener.OnItemLongClickListener;
import com.senierr.adapter.support.bean.LoadMoreBean;
import com.senierr.adapter.support.bean.StateBean;
import com.senierr.adapter.support.wrapper.BaseLoadMoreWrapper;
import com.senierr.demo.wrapper.FirstWrapper;
import com.senierr.demo.wrapper.LoadMoreWrapper;
import com.senierr.demo.wrapper.SecondWrapper;
import com.senierr.demo.wrapper.StateWrapper;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
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
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        multiTypeAdapter = new MultiTypeAdapter();

        FirstWrapper firstWrapper = new FirstWrapper();
        // 列表点击事件
        firstWrapper.setOnItemClickListener(new OnItemClickListener<DataBean>() {
            @Override
            public void onClick(ViewHolder viewHolder, int position, DataBean dataBean) {
                showToast("ItemClick: " + dataBean.getContent());
            }
        });
        firstWrapper.setOnItemLongClickListener(new OnItemLongClickListener<DataBean>() {
            @Override
            public boolean onLongClick(ViewHolder viewHolder, int position, DataBean dataBean) {
                showToast("ItemLongClick: " + dataBean.getContent());
                return true;
            }
        });
        // 子控件点击事件
        firstWrapper.setOnChildClickListener(R.id.btn_click, new OnChildClickListener<DataBean>() {
            @Override
            public void onClick(ViewHolder viewHolder, View view, int position, DataBean dataBean) {
                showToast("ChildClick: " + dataBean.getContent());
            }
        });
        firstWrapper.setOnChildLongClickListener(R.id.btn_click, new OnChildLongClickListener<DataBean>() {
            @Override
            public boolean onLongClick(ViewHolder viewHolder, View view, int position, DataBean dataBean) {
                showToast("ChildLongClick: " + dataBean.getContent());
                return true;
            }
        });
        SecondWrapper secondWrapper = new SecondWrapper();

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
        stateWrapper.setOnItemClickListener(new OnItemClickListener<StateBean>() {
            @Override
            public void onClick(ViewHolder viewHolder, int position, StateBean stateBean) {
                pageIndex = 1;
                loadData();
            }
        });

        multiTypeAdapter.bind(DataBean.class, firstWrapper, secondWrapper)
                .with(new DataBinder<DataBean>() {
                    @Override
                    public int onBindIndex(@NonNull DataBean item) {
                        if (item.getId() % 2 == 0) {
                            return 0;
                        }
                        return 1;
                    }
                });
        multiTypeAdapter.bind(StateBean.class, stateWrapper);
        multiTypeAdapter.bind(LoadMoreBean.class, loadMoreWrapper);
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
        if (id == R.id.action_default) {
            stateWrapper.setState(StateWrapper.STATE_DEFAULT);
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
                    multiTypeAdapter.clearData();
                    multiTypeAdapter.addDatas(dataBeanList);
                    multiTypeAdapter.addData(loadMoreWrapper.getLoadMoreBean());
                    multiTypeAdapter.notifyDataSetChanged();
                    pageIndex++;
                } else if (pageIndex == 3) {
                    loadMoreWrapper.loadNoMore();
//                        loadMoreWrapper.loadMoreFailure();
                } else {
                    loadMoreWrapper.loadCompleted();
                    int startPosition = multiTypeAdapter.getDataList().size() - 1;
                    multiTypeAdapter.addDatas(startPosition, dataBeanList);
                    multiTypeAdapter.notifyItemRangeInserted(startPosition, dataBeanList.size());
                    pageIndex++;
                }
            }
        }, 1000);
    }

    private void showToast(String toastStr) {
        Toast.makeText(this, toastStr, Toast.LENGTH_SHORT).show();
    }
}
