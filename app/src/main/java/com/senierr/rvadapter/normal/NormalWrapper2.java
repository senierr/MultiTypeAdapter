package com.senierr.rvadapter.normal;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.senierr.adapterlib.ViewHolderWrapper;
import com.senierr.adapterlib.util.RVHolder;
import com.senierr.rvadapter.R;
import com.senierr.rvadapter.bean.NormalBean;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class NormalWrapper2 extends ViewHolderWrapper<NormalBean> {

    @Override
    public int getLayoutId() {
        return R.layout.layout_normal_2;
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull NormalBean item) {
        TextView textView = holder.getView(R.id.tv_text);
        textView.setText(NormalWrapper2.class.getSimpleName() + ": " + item.getContent());
    }

    @Override
    public int getSpanSize(NormalBean item) {
        return item.getId();
    }
}
