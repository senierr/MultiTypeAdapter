package com.senierr.demo.bean;

/**
 * @author zhouchunjie
 * @date 2017/9/26
 */

public class FooterBean {

    private int id;
    private String content;

    public FooterBean() {
    }

    public FooterBean(int id, String content) {
        this.id = id;
        this.content = content;
    }

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
}
