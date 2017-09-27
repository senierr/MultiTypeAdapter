package com.senierr.adapterlib.listener;

import com.senierr.adapterlib.util.RVHolder;

/**
 * 普通类型点击回调
 *
 * Created by zhouchunjie on 2017/3/27.
 */

public abstract class OnClickListener {

    public void onClick(RVHolder viewHolder, int position) {}

    public boolean onLongClick(RVHolder viewHolder, int position) {
        return false;
    }
}
