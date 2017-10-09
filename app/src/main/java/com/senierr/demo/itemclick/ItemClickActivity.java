package com.senierr.demo.itemclick;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.senierr.rvadapter.RVAdapter;
import com.senierr.rvadapter.listener.OnItemChildClickListener;
import com.senierr.rvadapter.listener.OnItemClickListener;
import com.senierr.rvadapter.util.RVHolder;
import com.senierr.demo.BaseActivity;
import com.senierr.demo.R;
import com.senierr.demo.bean.NormalBean;
import com.senierr.rvadapter.util.RVItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ItemClickActivity extends BaseActivity {

    protected RecyclerView recyclerView;
    private RVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        initView();
        loadData();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new RVItemDecoration(this, R.dimen.divider_size, R.color.bg_divider));

        rvAdapter = new RVAdapter();

        ItemClickWrapper itemClickWrapper = new ItemClickWrapper();
        // 列表点击事件
        itemClickWrapper.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(RVHolder viewHolder, int position) {
                showToast("Click: " + position);
            }

            @Override
            public boolean onLongClick(RVHolder viewHolder, int position) {
                showToast("LongClick: " + position);
                return true;
            }
        });
        // 子控件点击事件
        itemClickWrapper.setOnItemChildClickListener(R.id.btn_1, new OnItemChildClickListener() {
            @Override
            public void onClick(RVHolder viewHolder, View view, int position) {
                showToast(position + ", Click: " + ((Button) view).getText());
            }

            @Override
            public boolean onLongClick(RVHolder viewHolder, View view, int position) {
                showToast(position + ", LongClick: " + ((Button) view).getText());
                return true;
            }
        });

        rvAdapter.assign(NormalBean.class)
                .to(itemClickWrapper);
        recyclerView.setAdapter(rvAdapter);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 80; i++) {
            list.add(new NormalBean(i, "Item: " + i));
        }
        rvAdapter.setItems(list);
        rvAdapter.notifyDataSetChanged();
    }
}
