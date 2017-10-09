package com.senierr.demo;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.senierr.rvadapter.util.RVItemDecoration;

public class BaseRecyclerViewActivity extends AppCompatActivity {

    protected RecyclerView recyclerView;
    protected Toast toast = null;

    protected void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // 设置布局类型
//        int orientation = OrientationHelper.VERTICAL;
//        int orientation = OrientationHelper.HORIZONTAL;
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, orientation, false));
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, orientation, false));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, orientation));
        // 每个Item确定高度，设置提高性能
        recyclerView.setHasFixedSize(true);
        // 设置分割线
        RVItemDecoration rvItemDecoration = new RVItemDecoration(this, R.dimen.divider_size, R.color.bg_divider);
        recyclerView.addItemDecoration(rvItemDecoration);
    }

    protected void showToast(String toastStr) {
        if (toast == null) {
            toast = Toast.makeText(this, toastStr, Toast.LENGTH_SHORT);
        }
        toast.setText(toastStr);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
