package com.senierr.adapter.support.bean;

/**
 * 加载更多实体类
 *
 * @author zhouchunjie
 * @date 2017/11/3
 */
public class LoadMoreBean {

    public final static int STATUS_LOADING = 101;
    public final static int STATUS_COMPLETED = 102;
    public final static int STATUS_NO_MORE = 103;
    public final static int STATUS_FAILURE = 104;

    private int loadState;

    public int getLoadState() {
        return loadState;
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
    }
}
