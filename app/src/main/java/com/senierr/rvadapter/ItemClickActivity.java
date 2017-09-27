package com.senierr.rvadapter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import com.senierr.adapterlib.RVAdapter;
import com.senierr.adapterlib.listener.OnChildClickListener;
import com.senierr.adapterlib.listener.OnClickListener;
import com.senierr.adapterlib.listener.OnItemClickListener;
import com.senierr.adapterlib.util.RVHolder;
import com.senierr.rvadapter.bean.NormalBean;
import com.senierr.rvadapter.wrapper.ItemClickWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemClickActivity extends BaseRecyclerViewActivity {

    private RVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        initRecyclerView();
        initView();
        loadData();
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rvAdapter = new RVAdapter();

        ItemClickWrapper itemClickWrapper = new ItemClickWrapper();
        itemClickWrapper.setOnClickListener(new OnClickListener() {
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

        itemClickWrapper.setOnChildClickListener(new OnChildClickListener(R.id.btn_1) {
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
        itemClickWrapper.setOnChildClickListener(new OnChildClickListener(R.id.btn_2) {
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

        rvAdapter.register(NormalBean.class)
                .with(itemClickWrapper);
        recyclerView.setAdapter(rvAdapter);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 80; i++) {
            list.add(new NormalBean(i, "" + i));
        }
        rvAdapter.setItems(list);
        rvAdapter.notifyDataSetChanged();
    }
}
