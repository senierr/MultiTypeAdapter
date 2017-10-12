package com.senierr.adapter.exception;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */
public class LayoutManager404Exception extends IllegalArgumentException {

    public LayoutManager404Exception() {
        super("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, " +
                "GridLayoutManager and StaggeredGridLayoutManager.");
    }
}
