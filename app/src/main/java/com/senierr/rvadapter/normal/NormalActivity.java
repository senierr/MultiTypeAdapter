package com.senierr.rvadapter.normal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.senierr.adapterlib.RVAdapter;
import com.senierr.adapterlib.ViewHolderWrapper;
import com.senierr.adapterlib.binder.OneToManyBinder;
import com.senierr.rvadapter.BaseRecyclerViewActivity;
import com.senierr.rvadapter.R;
import com.senierr.rvadapter.bean.NormalBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NormalActivity extends BaseRecyclerViewActivity {

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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // 设置布局类型
        int orientation = OrientationHelper.VERTICAL;
//        int orientation = OrientationHelper.HORIZONTAL;
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, orientation, false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, orientation, false));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, orientation));
        // 创建适配器
        rvAdapter = new RVAdapter();
        // 一对多
        rvAdapter.register(NormalBean.class)
                .with(new NormalWrapper1(),
                        new NormalWrapper2())
                .by(new OneToManyBinder<NormalBean>() {
                    @Override
                    public Class<? extends ViewHolderWrapper<NormalBean>> onGetWrapperType(@NonNull NormalBean item) {
                        if (item.getId() < 3) {
                            return NormalWrapper1.class;
                        }
                        return NormalWrapper2.class;
                    }
                });
        // 设置适配器
        recyclerView.setAdapter(rvAdapter);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 80; i++) {
            NormalBean normalBean = new NormalBean();
            Random random = new Random();
            normalBean.setId(random.nextInt(4) + 1);
            normalBean.setContent("Item: " + i);
            list.add(normalBean);
        }
        rvAdapter.setItems(list);
        rvAdapter.notifyDataSetChanged();
    }
}
