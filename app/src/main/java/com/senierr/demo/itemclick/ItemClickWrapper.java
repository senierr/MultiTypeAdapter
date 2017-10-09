package com.senierr.demo.itemclick;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.senierr.rvadapter.ViewHolderWrapper;
import com.senierr.rvadapter.util.RVHolder;
import com.senierr.demo.R;
import com.senierr.demo.bean.NormalBean;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */

public class ItemClickWrapper extends ViewHolderWrapper<NormalBean> {

    @Override
    public int getLayoutId() {
        return R.layout.layout_item_click;
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolder holder, @NonNull NormalBean item) {
        TextView textView = holder.getView(R.id.tv_text);
        textView.setText(item.getContent());
    }

    @Override
    public void onViewHolderCreate(@NonNull final RVHolder holder) {
        holder.getView(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), holder.getLayoutPosition() + ", Click: " + ((Button) view).getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}