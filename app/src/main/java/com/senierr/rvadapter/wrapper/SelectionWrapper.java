package com.senierr.rvadapter.wrapper;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.TextView;

import com.senierr.adapterlib.ViewWrapper;
import com.senierr.adapterlib.util.RVHolder;
import com.senierr.rvadapter.R;
import com.senierr.rvadapter.bean.SelectionBean;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class SelectionWrapper extends ViewWrapper<SelectionBean> {

    @Override
    public int getLayoutId() {
        return R.layout.layout_selection;
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull SelectionBean item) {
        TextView textView = holder.getView(R.id.tv_text);
        textView.setText(item.getContent());
    }

    @Override
    public int getSpanSize(SelectionBean item) {
        return 4;
    }
}
