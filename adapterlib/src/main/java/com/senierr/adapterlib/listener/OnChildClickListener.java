package com.senierr.adapterlib.listener;

import android.support.annotation.IdRes;
import android.view.View;

import com.senierr.adapterlib.util.RVHolder;

public abstract class OnChildClickListener {

    public @IdRes int childId;

    public OnChildClickListener(int childId) {
        this.childId = childId;
    }

    public void onClick(RVHolder viewHolder, View view, int position) {}

    public boolean onLongClick(RVHolder viewHolder, View view, int position) {
        return false;
    }
}
