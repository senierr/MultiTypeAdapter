package com.senierr.demo.wrapper;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.senierr.rvadapter.ViewHolderWrapper;
import com.senierr.rvadapter.util.RVHolder;
import com.senierr.demo.R;
import com.senierr.demo.bean.FooterBean;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class FooterWrapper extends ViewHolderWrapper<FooterBean> {

    @Override
    public int getLayoutId() {
        return R.layout.layout_footer;
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull FooterBean item) {
        TextView textView = holder.getView(R.id.tv_text);
        textView.setText(item.getContent());
    }

    @Override
    public int getSpanSize(FooterBean item) {
        return 4;
    }
}
