package com.senierr.demo.normal;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.senierr.rvadapter.ViewHolderWrapper;
import com.senierr.rvadapter.util.RVHolder;
import com.senierr.demo.R;
import com.senierr.demo.bean.NormalBean;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class NormalWrapper1 extends ViewHolderWrapper<NormalBean> {

    @Override
    public int getLayoutId() {
        return R.layout.layout_normal_1;
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull NormalBean item) {
        TextView textView = holder.getView(R.id.tv_text);
        textView.setText(NormalWrapper1.class.getSimpleName() + ": " + item.getContent());
    }

    @Override
    public int getSpanSize(NormalBean item) {
        return item.getId();
    }
}
