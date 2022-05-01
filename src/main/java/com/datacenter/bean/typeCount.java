package com.datacenter.bean;

public class typeCount {
    private String news_type;

    private Integer count;

    public typeCount(String news_type, Integer count) {
        this.news_type = news_type;
        this.count = count;
    }

    public typeCount() {

    }

    public String getNews_type() {
        return news_type;
    }

    public void setNews_type(String news_type) {
        this.news_type = news_type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "typeCount{" +
                "news_type='" + news_type + '\'' +
                ", count=" + count +
                '}';
    }
}
