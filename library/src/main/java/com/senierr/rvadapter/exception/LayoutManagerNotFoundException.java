package com.senierr.rvadapter.exception;

/**
 * @author zhouchunjie
 * @date 2017/9/25
 */
public class LayoutManagerNotFoundException extends RuntimeException {

    public LayoutManagerNotFoundException() {
        super("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, " +
                "GridLayoutManager and StaggeredGridLayoutManager.");
    }
}
