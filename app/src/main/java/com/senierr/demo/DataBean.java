package com.senierr.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author zhouchunjie
 * @date 2017/9/26
 */

public class DataBean {

    private int id;
    private String content;
    private int height;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public static List<DataBean> getData(int pageIndex, int pageSize) {
        List<DataBean> dataBeanList = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            DataBean dataBean = new DataBean();
            dataBean.setId(i);
            dataBean.setContent(pageIndex + "-" + i);
            Random random = new Random();
            dataBean.setHeight(random.nextInt(300) + 100);
            dataBeanList.add(dataBean);
        }
        return dataBeanList;
    }
}
