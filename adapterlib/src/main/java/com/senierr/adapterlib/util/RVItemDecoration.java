package com.senierr.adapterlib.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;

public class RVItemDecoration extends RecyclerView.ItemDecoration {

    // 布局方向
    private int orientation = -1;
    // 粗细
    private int dividerSize;
    // 分割线
    private Drawable dividerDrawable;
    // 子布局
    private final Rect childBounds = new Rect();
    // 状态缓存
    private SparseArray<SpanState> stateSparseArray = new SparseArray<>();

    public RVItemDecoration(Context context, @DimenRes int dividerSize, @ColorRes int color) {
        this.dividerSize = context.getResources().getDimensionPixelSize(dividerSize);
        dividerDrawable = new ColorDrawable(ContextCompat.getColor(context, color));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int currentPosition = parent.getChildLayoutPosition(view);
        if (currentPosition == -1) return;

        outRect.set(0, 0, dividerSize, dividerSize);

        SpanState spanState = getSpanState(view, parent, state);
        if (orientation == OrientationHelper.VERTICAL) {
            if (spanState.isLastSpanGroup) {
                outRect.bottom = 0;
            }
            if (spanState.isEndSpan) {
                outRect.right = 0;
            }
        } else {
            if (spanState.isLastSpanGroup) {
                outRect.right = 0;
            }
            if (spanState.isEndSpan) {
                outRect.bottom = 0;
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, childBounds);

            SpanState spanState = getSpanState(child, parent, state);
            if (orientation == OrientationHelper.VERTICAL) {
                // 画垂直分割线
                if (!spanState.isEndSpan) {
                    dividerDrawable.setBounds(childBounds.right - dividerSize, childBounds.top,
                            childBounds.right, childBounds.bottom);
                    dividerDrawable.draw(c);
                }
                // 画水平分割线
                if (!spanState.isLastSpanGroup) {
                    dividerDrawable.setBounds(childBounds.left, childBounds.bottom - dividerSize,
                            childBounds.right, childBounds.bottom);
                    dividerDrawable.draw(c);
                }
            } else {
                // 画垂直分割线
                if (!spanState.isLastSpanGroup) {
                    dividerDrawable.setBounds(childBounds.right - dividerSize, childBounds.top,
                            childBounds.right, childBounds.bottom);
                    dividerDrawable.draw(c);
                }
                // 画水平分割线
                if (!spanState.isEndSpan) {
                    dividerDrawable.setBounds(childBounds.left, childBounds.bottom - dividerSize,
                            childBounds.right, childBounds.bottom);
                    dividerDrawable.draw(c);
                }
            }
        }
    }

    /**
     * 获取Span状态
     *
     * @param view
     * @param parent
     * @param state
     * @return
     */
    private SpanState getSpanState(View view, RecyclerView parent, RecyclerView.State state) {
        final int currentPosition = parent.getChildLayoutPosition(view);
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        SpanState spanState = stateSparseArray.get(currentPosition);
        if (spanState == null) {
            spanState = new SpanState();

            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
                if (orientation < 0) {
                    orientation = gridLayoutManager.getOrientation();
                }

                int spanCount = gridLayoutManager.getSpanCount();
                int spanSize = gridLayoutManager.getSpanSizeLookup().getSpanSize(currentPosition);
                int spanIndex = gridLayoutManager.getSpanSizeLookup().getSpanIndex(currentPosition, spanCount);
                int spanGroupIndex = gridLayoutManager.getSpanSizeLookup().getSpanGroupIndex(currentPosition, spanCount);
                // 判断是否是最后一组Span
                spanState.isLastSpanGroup = true;
                for (int i = currentPosition + 1; i < currentPosition + 1 + spanCount; i++) {
                    if (i >= state.getItemCount()) {
                        break;
                    }
                    int groupIndex = gridLayoutManager.getSpanSizeLookup().getSpanGroupIndex(i, spanCount);
                    if (groupIndex > spanGroupIndex) {
                        spanState.isLastSpanGroup = false;
                        break;
                    }
                }
                // 判断是否是组末Span
                spanState.isEndSpan = spanIndex + spanSize == spanCount;
            } else if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (orientation < 0) {
                    orientation = linearLayoutManager.getOrientation();
                }

                spanState.isLastSpanGroup = state.getItemCount() - currentPosition - 1 <= 0;
                spanState.isEndSpan = true;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (orientation < 0) {
                    orientation = staggeredGridLayoutManager.getOrientation();
                }

                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();

                int spanCount = staggeredGridLayoutManager.getSpanCount();
                int spanIndex = layoutParams.getSpanIndex();

                spanState.isLastSpanGroup = layoutParams.isFullSpan() && currentPosition == state.getItemCount() - 1;
                spanState.isEndSpan = layoutParams.isFullSpan() || (!layoutParams.isFullSpan() && spanIndex == spanCount - 1);
            }
            stateSparseArray.put(currentPosition, spanState);
        }
        return spanState;
    }

    private class SpanState {
        boolean isLastSpanGroup = false;
        boolean isEndSpan = false;

        @Override
        public String toString() {
            return "SpanState{" +
                    "isLastSpanGroup=" + isLastSpanGroup +
                    ", isEndSpan=" + isEndSpan +
                    '}';
        }
    }
}
