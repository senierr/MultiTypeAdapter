package com.senierr.adapter.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * @author zhouchunjie
 * @date 2017/6/22
 */

public class RecyclerViewUtil {

    /**
     * 获取滚动方向
     *
     * @param recyclerView
     * @return
     */
    public static int getOrientation(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int orientation;
        if (layoutManager instanceof GridLayoutManager) {
            orientation = ((GridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        } else {
            throw new IllegalArgumentException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, " +
                    "GridLayoutManager and StaggeredGridLayoutManager.");
        }
        return orientation;
    }

    /**
     * 获取SpanCount
     *
     * @param recyclerView
     * @return
     */
    public static int getSpanCount(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int spanCount;
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof LinearLayoutManager) {
            spanCount = 1;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        } else {
            throw new IllegalArgumentException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, " +
                    "GridLayoutManager and StaggeredGridLayoutManager.");
        }
        return spanCount;
    }

    /**
     * 获第一个可视Item
     */
    public static int getFirstVisibleItemPosition(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        int firstVisibleItemPosition;
        if (layoutManager instanceof GridLayoutManager) {
            firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastPositions = staggeredGridLayoutManager
                    .findFirstVisibleItemPositions(new int[staggeredGridLayoutManager.getSpanCount()]);
            firstVisibleItemPosition = findMin(lastPositions);
        } else {
            throw new IllegalArgumentException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, " +
                    "GridLayoutManager and StaggeredGridLayoutManager.");
        }
        return firstVisibleItemPosition;
    }

    /**
     * 获取最后一个可视Item
     */
    public static int getLastVisibleItemPosition(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        int lastVisibleItemPosition;
        if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] lastPositions = staggeredGridLayoutManager
                    .findLastVisibleItemPositions(new int[staggeredGridLayoutManager.getSpanCount()]);
            lastVisibleItemPosition = findMax(lastPositions);
        } else {
            throw new IllegalArgumentException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, " +
                    "GridLayoutManager and StaggeredGridLayoutManager.");
        }
        return lastVisibleItemPosition;
    }

    private static int findMin(int[] positions) {
        int min = positions[0];
        for (int value : positions) {
            min = Math.min(value, min);
        }
        return min;
    }

    private static int findMax(int[] positions) {
        int max = positions[0];
        for (int value : positions) {
            max = Math.max(value, max);
        }
        return max;
    }
}
