package com.example.memoapp.bean;

public class InfoBean {
    private int id;
    private String content;
    private int attribute;
    private String date;

    public InfoBean(int id, String content, int attribute, String date) {
        this.id = id;
        this.content = content;
        this.attribute = attribute;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getAttribute() {
        return attribute;
    }

    public String getDate() {
        return date;
    }
}
