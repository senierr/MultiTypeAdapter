package com.senierr.rvadapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.senierr.adapterlib.RVAdapter;
import com.senierr.adapterlib.ViewWrapper;
import com.senierr.adapterlib.binder.OneToManyBinder;
import com.senierr.adapterlib.listener.OnItemClickListener;
import com.senierr.adapterlib.util.RVHolder;
import com.senierr.adapterlib.util.RVItemDecoration;
import com.senierr.rvadapter.bean.FooterBean;
import com.senierr.rvadapter.bean.HeaderBean;
import com.senierr.rvadapter.bean.NormalBean;
import com.senierr.rvadapter.bean.SelectionBean;
import com.senierr.rvadapter.wrapper.FooterWrapper;
import com.senierr.rvadapter.wrapper.HeaderWrapper;
import com.senierr.rvadapter.wrapper.NormalWrapper1;
import com.senierr.rvadapter.wrapper.NormalWrapper2;
import com.senierr.rvadapter.wrapper.SelectionWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmptyAndErrorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;

    private Toast toast = null;

    public void showToast(String toastStr) {
        if (toast == null) {
            toast = Toast.makeText(this, toastStr, Toast.LENGTH_SHORT);
        }
        toast.setText(toastStr);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

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
        // 每个Item确定高度，设置提高性能
        recyclerView.setHasFixedSize(true);
        // 设置分割线
        RVItemDecoration rvItemDecoration = new RVItemDecoration(this, R.dimen.dimen_4, R.color.transparent);
        recyclerView.addItemDecoration(rvItemDecoration);
        // 创建适配器
        rvAdapter = new RVAdapter();
        // 一对一
        rvAdapter.register(SelectionBean.class)
                .with(new SelectionWrapper());
        rvAdapter.register(FooterBean.class)
                .with(new FooterWrapper());
        rvAdapter.register(HeaderBean.class)
                .with(new HeaderWrapper());
        // 一对多
        rvAdapter.register(NormalBean.class)
                .with(new NormalWrapper1(),
                        new NormalWrapper2())
                .by(new OneToManyBinder<NormalBean>() {
                    @Override
                    public Class<? extends ViewWrapper<NormalBean>> onGetWrapperType(@NonNull NormalBean item) {
                        if (item.getId() < 3) {
                            return NormalWrapper1.class;
                        }
                        return NormalWrapper2.class;
                    }
                });
        // 设置点击事件
        rvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RVHolder viewHolder, int position) {
                showToast("onItemClick: " + position);
            }

            @Override
            public boolean onItemLongClick(RVHolder viewHolder, int position) {
                showToast("onItemLongClick: " + position);
                return true;
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
            if (i < 1) {
                list.add(new HeaderBean(i, "Header: " + i));
            } else if (i % 8 == 0) {
                list.add(new SelectionBean(i, "Selection: " + i));
            } else if (i > 78) {
                list.add(new FooterBean(i, "Footer: " + i));
            } else {
                NormalBean normalBean = new NormalBean();
                Random random = new Random();
                normalBean.setId(random.nextInt(4) + 1);
                normalBean.setContent("Normal: " + i);
                list.add(normalBean);
            }
        }
        rvAdapter.setItems(list);
        rvAdapter.notifyDataSetChanged();
    }
}
