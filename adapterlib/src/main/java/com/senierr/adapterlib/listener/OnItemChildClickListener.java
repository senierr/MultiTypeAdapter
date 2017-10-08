package com.senierr.adapterlib.listener;

import android.view.View;
import com.senierr.adapterlib.util.RVHolder;

/**
 * 列表子控件点击回调
 *
 * @author zhouchunjie
 * @date 2017/9/25
 */

public abstract class OnItemChildClickListener {

    public void onClick(RVHolder viewHolder, View view, int position) {}

    public boolean onLongClick(RVHolder viewHolder, View view, int position) {
        return false;
    }
}
