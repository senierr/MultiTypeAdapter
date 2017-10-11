package com.senierr.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.senierr.rvadapter.RVAdapter;
import com.senierr.rvadapter.listener.OnItemChildClickListener;
import com.senierr.rvadapter.listener.OnItemClickListener;
import com.senierr.rvadapter.support.BaseLoadMoreWrapper;
import com.senierr.rvadapter.util.RVHolder;
import com.senierr.rvadapter.util.RVItemDecoration;

public class MainActivity extends AppCompatActivity {

    private Toast toast;
    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;

    private LoadMoreWrapper loadMoreWrapper;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RVItemDecoration(this, R.dimen.dimen_4, R.color.transparent));

        rvAdapter = new RVAdapter();

        FirstWrapper firstWrapper = new FirstWrapper();
        // 列表点击事件
        firstWrapper.setOnItemClickListener(new OnItemClickListener() {
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
        firstWrapper.setOnItemChildClickListener(R.id.btn_click, new OnItemChildClickListener() {
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

        loadMoreWrapper = new LoadMoreWrapper();
        loadMoreWrapper.setOnLoadMoreListener(new BaseLoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int size = rvAdapter.getDataList().size();

                        if (size > 35) {
                            loadMoreWrapper.loadMoreFailure();
                            return;
                        }

                        rvAdapter.getDataList().add(size - 1, new DataBean(size - 1,
                                "Item: " + (size - 1)));
                        loadMoreWrapper.loadMoreCompleted();
                        rvAdapter.notifyItemChanged(rvAdapter.getItemCount() - 2);
                    }
                }, 2000);
            }
        });


        rvAdapter.addViewHolderWrappers(firstWrapper,
                new SecondWrapper(),
                new ThirdWrapper(),
                loadMoreWrapper);
        recyclerView.setAdapter(rvAdapter);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        for (int i = 0; i < 30; i++) {
            rvAdapter.getDataList().add(new DataBean(i, "Item: " + i));
        }
        rvAdapter.getDataList().add(loadMoreWrapper.getLoadMoreBean());
        rvAdapter.notifyDataSetChanged();
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
