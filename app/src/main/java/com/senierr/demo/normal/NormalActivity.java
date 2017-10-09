package com.senierr.demo.normal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.senierr.rvadapter.RVAdapter;
import com.senierr.rvadapter.ViewHolderWrapper;
import com.senierr.rvadapter.link.OneToManyLink;
import com.senierr.demo.BaseActivity;
import com.senierr.demo.R;
import com.senierr.demo.bean.NormalBean;
import com.senierr.rvadapter.util.RVHolder;

import java.util.ArrayList;
import java.util.List;

public class NormalActivity extends BaseActivity {

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
        rvAdapter = new RVAdapter();
        rvAdapter.assign(NormalBean.class)
                .to(new ViewHolderWrapper<NormalBean>() {
                        @Override
                        public int getLayoutId() {
                            return R.layout.item_normal_left;
                        }

                        @Override
                        public void onBindViewHolder(@NonNull RVHolder holder, @NonNull NormalBean item) {
                            holder.setText(R.id.tv_text, "Left-" + item.getContent());
                        }
                    },
                        new ViewHolderWrapper<NormalBean>() {
                            @Override
                            public int getLayoutId() {
                                return R.layout.item_normal_right;
                            }

                            @Override
                            public void onBindViewHolder(@NonNull RVHolder holder, @NonNull NormalBean item) {
                                holder.setText(R.id.tv_text, "Right-" + item.getContent());
                            }
                        })
                .by(new OneToManyLink<NormalBean>() {
                    @Override
                    public Class<? extends ViewHolderWrapper<NormalBean>> onAssignedWithType(@NonNull NormalBean item) {
                        return null;
                    }

                    @Override
                    public int onAssignedWithIndex(@NonNull NormalBean item) {
                        if (item.getId() % 2 == 0) {
                            return 0;
                        }
                        return 1;
                    }
                });
        recyclerView.setAdapter(rvAdapter);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 80; i++) {
            NormalBean normalBean = new NormalBean();
            normalBean.setId(i);
            normalBean.setContent("Item: " + i);
            list.add(normalBean);
        }
        rvAdapter.setItems(list);
        rvAdapter.notifyDataSetChanged();
    }
}
