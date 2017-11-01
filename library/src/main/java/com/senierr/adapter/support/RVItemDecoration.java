package com.senierr.adapter.support;

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

    public RVItemDecoration(Context context, @DimenRes int dividerSize, @ColorRes int color) {
        this.dividerSize = context.getResources().getDimensionPixelSize(dividerSize);
        dividerDrawable = new ColorDrawable(ContextCompat.getColor(context, color));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int currentPosition = parent.getChildLayoutPosition(view);
        if (currentPosition == -1) return;

        SpanState spanState = getSpanState(view, parent, state);
        int eachSize = (spanState.spanCount - 1) * dividerSize / spanState.spanCount;
        if (orientation == OrientationHelper.VERTICAL) {
            // top, bottom
            if (spanState.isFirstSpanGroup && spanState.isLastSpanGroup) {
                outRect.top = 0;
                outRect.bottom = 0;
            } else if (spanState.isFirstSpanGroup) {
                outRect.top = 0;
                outRect.bottom = eachSize;
            } else if (spanState.isLastSpanGroup) {
                outRect.top = eachSize;
                outRect.bottom = 0;
            } else {
                outRect.top = eachSize / 2;
                outRect.bottom = eachSize / 2;
            }
            // left, right
            if (spanState.isStartSpan && spanState.isEndSpan) {
                outRect.left = 0;
                outRect.right = 0;
            } else if (spanState.isStartSpan) {
                outRect.left = 0;
                outRect.right = eachSize;
            } else if (spanState.isEndSpan) {
                outRect.left = eachSize;
                outRect.right = 0;
            } else {
                outRect.left = eachSize / 2;
                outRect.right = eachSize / 2;
            }
        } else {
            // left, right
            if (spanState.isFirstSpanGroup && spanState.isLastSpanGroup) {
                outRect.left = 0;
                outRect.right = 0;
            } else if (spanState.isFirstSpanGroup) {
                outRect.left = 0;
                outRect.right = eachSize;
            } else if (spanState.isLastSpanGroup) {
                outRect.left = eachSize;
                outRect.right = 0;
            } else {
                outRect.left = eachSize / 2;
                outRect.right = eachSize / 2;
            }
            // top, bottom
            if (spanState.isStartSpan && spanState.isEndSpan) {
                outRect.top = 0;
                outRect.bottom = 0;
            } else if (spanState.isStartSpan) {
                outRect.top = 0;
                outRect.bottom = eachSize;
            } else if (spanState.isEndSpan) {
                outRect.top = eachSize;
                outRect.bottom = 0;
            } else {
                outRect.top = eachSize / 2;
                outRect.bottom = eachSize / 2;
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
            int eachSize = (spanState.spanCount - 1) * dividerSize / spanState.spanCount;
            if (orientation == OrientationHelper.VERTICAL) {
                // top, bottom
                if (!spanState.isFirstSpanGroup && !spanState.isLastSpanGroup) {
                    dividerDrawable.setBounds(childBounds.left, childBounds.top,
                            childBounds.right, childBounds.top +  eachSize / 2);
                    dividerDrawable.draw(c);
                    dividerDrawable.setBounds(childBounds.left, childBounds.bottom - eachSize / 2,
                            childBounds.right, childBounds.bottom);
                    dividerDrawable.draw(c);
                } else if (!spanState.isFirstSpanGroup) {
                    dividerDrawable.setBounds(childBounds.left, childBounds.top,
                            childBounds.right, childBounds.top +  eachSize);
                    dividerDrawable.draw(c);
                } else if (!spanState.isLastSpanGroup) {
                    dividerDrawable.setBounds(childBounds.left, childBounds.bottom - eachSize,
                            childBounds.right, childBounds.bottom);
                    dividerDrawable.draw(c);
                }
                // left, right
                if (!spanState.isStartSpan && !spanState.isEndSpan) {
                    dividerDrawable.setBounds(childBounds.left, childBounds.top,
                            childBounds.left + eachSize / 2, childBounds.bottom);
                    dividerDrawable.draw(c);
                    dividerDrawable.setBounds(childBounds.right - eachSize / 2, childBounds.top,
                            childBounds.right, childBounds.bottom);
                    dividerDrawable.draw(c);
                } else if (!spanState.isStartSpan) {
                    dividerDrawable.setBounds(childBounds.left, childBounds.top,
                            childBounds.left + eachSize, childBounds.bottom);
                    dividerDrawable.draw(c);
                } else if (!spanState.isEndSpan) {
                    dividerDrawable.setBounds(childBounds.right - eachSize, childBounds.top,
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

                // left, right
                if (!spanState.isFirstSpanGroup && !spanState.isLastSpanGroup) {
                    dividerDrawable.setBounds(childBounds.left, childBounds.top,
                            childBounds.left + eachSize / 2, childBounds.bottom);
                    dividerDrawable.draw(c);
                    dividerDrawable.setBounds(childBounds.right - eachSize / 2, childBounds.top,
                            childBounds.right, childBounds.bottom);
                    dividerDrawable.draw(c);
                } else if (!spanState.isFirstSpanGroup) {
                    dividerDrawable.setBounds(childBounds.left, childBounds.top,
                            childBounds.left + eachSize, childBounds.bottom);
                    dividerDrawable.draw(c);
                } else if (!spanState.isLastSpanGroup) {
                    dividerDrawable.setBounds(childBounds.right - eachSize, childBounds.top,
                            childBounds.right, childBounds.bottom);
                    dividerDrawable.draw(c);
                }
                // top, bottom
                if (!spanState.isStartSpan && !spanState.isEndSpan) {
                    dividerDrawable.setBounds(childBounds.left, childBounds.top,
                            childBounds.right, childBounds.top +  eachSize / 2);
                    dividerDrawable.draw(c);
                    dividerDrawable.setBounds(childBounds.left, childBounds.bottom - eachSize / 2,
                            childBounds.right, childBounds.bottom);
                    dividerDrawable.draw(c);
                } else if (!spanState.isStartSpan) {
                    dividerDrawable.setBounds(childBounds.left, childBounds.top,
                            childBounds.right, childBounds.top +  eachSize);
                    dividerDrawable.draw(c);
                } else if (!spanState.isEndSpan) {
                    dividerDrawable.setBounds(childBounds.left, childBounds.bottom - eachSize,
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
        SpanState spanState = new SpanState();

        final int currentPosition = parent.getChildLayoutPosition(view);
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
            if (orientation < 0) {
                orientation = gridLayoutManager.getOrientation();
            }

            int spanCount = gridLayoutManager.getSpanCount();
            int spanSize = gridLayoutManager.getSpanSizeLookup().getSpanSize(currentPosition);
            int spanIndex = gridLayoutManager.getSpanSizeLookup().getSpanIndex(currentPosition, spanCount);
            int spanGroupIndex = gridLayoutManager.getSpanSizeLookup().getSpanGroupIndex(currentPosition, spanCount);

            spanState.spanCount = spanCount;
            // 判断是否第一组
            if (spanGroupIndex == 0) {
                spanState.isFirstSpanGroup = true;
            }
            // 判断是否最后一组
            spanState.isLastSpanGroup = true;
            for (int i = currentPosition + 1; i < currentPosition + 1 + spanCount; i++) {
                // 判断是否越界
                if (i >= state.getItemCount()) {
                    break;
                }
                // 判断是否还有下一组
                int groupIndex = gridLayoutManager.getSpanSizeLookup().getSpanGroupIndex(i, spanCount);
                if (groupIndex > spanGroupIndex) {
                    spanState.isLastSpanGroup = false;
                    break;
                }
            }
            // 判断是否组始Span
            if (spanIndex == 0) {
                spanState.isStartSpan = true;
            }
            // 判断是否组末Span
            spanState.isEndSpan = spanIndex + spanSize == spanCount;
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (orientation < 0) {
                orientation = linearLayoutManager.getOrientation();
            }

            spanState.spanCount = 1;
            if (currentPosition == 0) {
                spanState.isFirstSpanGroup = true;
            }
            spanState.isLastSpanGroup = state.getItemCount() - currentPosition - 1 <= 0;
            spanState.isStartSpan = true;
            spanState.isEndSpan = true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            if (orientation < 0) {
                orientation = staggeredGridLayoutManager.getOrientation();
            }

            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();

            int spanCount = staggeredGridLayoutManager.getSpanCount();
            int spanIndex = layoutParams.getSpanIndex();

            spanState.spanCount = spanCount;
            spanState.isFirstSpanGroup = true;
            spanState.isLastSpanGroup = layoutParams.isFullSpan() && currentPosition == state.getItemCount() - 1;
            spanState.isStartSpan = layoutParams.isFullSpan() || (!layoutParams.isFullSpan() && spanIndex == 0);
            spanState.isEndSpan = layoutParams.isFullSpan() || (!layoutParams.isFullSpan() && spanIndex == spanCount - 1);
        }
        return spanState;
    }

    private class SpanState {
        int spanCount;
        boolean isFirstSpanGroup = false;
        boolean isLastSpanGroup = false;
        boolean isStartSpan = false;
        boolean isEndSpan = false;

        @Override
        public String toString() {
            return "SpanState {" +
                    "isFirstSpanGroup = " + isFirstSpanGroup +
                    ", isLastSpanGroup = " + isLastSpanGroup +
                    ", isStartSpan = " + isStartSpan +
                    ", isEndSpan = " + isEndSpan +
                    '}';
        }
    }
}
