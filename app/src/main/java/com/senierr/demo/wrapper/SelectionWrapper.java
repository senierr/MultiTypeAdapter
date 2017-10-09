package com.senierr.demo.wrapper;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.senierr.rvadapter.ViewHolderWrapper;
import com.senierr.rvadapter.util.RVHolder;
import com.senierr.demo.R;
import com.senierr.demo.bean.SelectionBean;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class SelectionWrapper extends ViewHolderWrapper<SelectionBean> {

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
