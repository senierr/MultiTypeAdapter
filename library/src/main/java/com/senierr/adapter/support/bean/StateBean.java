package com.senierr.adapter.support.bean;

/**
 * 状态实体类
 *
 * @author zhouchunjie
 * @date 2017/11/3
 */

public class StateBean {

    public final static int STATE_NONE = -101;
    public final static int STATE_LOADING = -102;
    public final static int STATE_EMPTY = -103;
    public final static int STATE_ERROR = -104;
    public final static int STATE_NO_NETWORK = -105;

    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
