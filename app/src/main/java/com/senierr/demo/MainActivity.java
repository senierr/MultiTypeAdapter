package com.senierr.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.senierr.adapter.MultiTypeAdapter;
import com.senierr.adapter.ViewHolderWrapper;
import com.senierr.adapter.listener.OnItemChildClickListener;
import com.senierr.adapter.listener.OnItemClickListener;
import com.senierr.adapter.support.BaseLoadMoreWrapper;
import com.senierr.adapter.util.RVHolder;
import com.senierr.adapter.util.RVItemDecoration;
import com.senierr.adapter.util.RecyclerViewUtil;

public class MainActivity extends AppCompatActivity {

    private Toast toast;
    private RecyclerView recyclerView;
    private MultiTypeAdapter multiTypeAdapter;

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
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RVItemDecoration(this, R.dimen.dimen_4, R.color.transparent));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        multiTypeAdapter = new MultiTypeAdapter();

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
                multiTypeAdapter.notifyDataSetChanged();
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
                        int size = multiTypeAdapter.getDataList().size();
                        if (size > 33) {
                            loadMoreWrapper.loadMoreFailure();

                            multiTypeAdapter.getDataList().clear();
                            multiTypeAdapter.getDataList().add("This is en empty page");
                            multiTypeAdapter.notifyDataSetChanged();
                            layoutManager.invalidateSpanAssignments();
                            layoutManager.scrollToPosition(0);
                            return;
                        }

                        multiTypeAdapter.getDataList().add(size - 1, new DataBean(size - 1,
                                "Item: " + (size - 1)));
                        multiTypeAdapter.notifyItemInserted(multiTypeAdapter.getItemCount() - 2);
                        loadMoreWrapper.loadMoreCompleted();
                    }
                }, 2000);
            }
        });

        multiTypeAdapter.register(firstWrapper,
                new SecondWrapper(),
                new ThirdWrapper(),
                loadMoreWrapper,
                new ViewHolderWrapper<String>(String.class, R.layout.item_empty) {
                    @Override
                    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull String item) {
                        holder.setText(R.id.tv_text, item);
                    }

                    @Override
                    public int getSpanSize(String item) {
                        return RecyclerViewUtil.getSpanCount(recyclerView);
                    }
                });
        recyclerView.setAdapter(multiTypeAdapter);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        for (int i = 0; i < 30; i++) {
            multiTypeAdapter.getDataList().add(new DataBean(i, "Item: " + i));
        }
        multiTypeAdapter.getDataList().add(loadMoreWrapper.getLoadMoreBean());
        multiTypeAdapter.notifyDataSetChanged();
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
