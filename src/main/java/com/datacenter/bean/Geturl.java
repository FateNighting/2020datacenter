package com.datacenter.bean;

public class Geturl {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column geturl.url_id
     *
     * @mbggenerated
     */
    private Integer urlId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column geturl.url_getname
     *
     * @mbggenerated
     */
    private String urlGetname;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column geturl.url_gettime
     *
     * @mbggenerated
     */
    private String urlGettime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column geturl.url_id
     *
     * @return the value of geturl.url_id
     *
     * @mbggenerated
     */
    public Integer getUrlId() {
        return urlId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column geturl.url_id
     *
     * @param urlId the value for geturl.url_id
     *
     * @mbggenerated
     */
    public void setUrlId(Integer urlId) {
        this.urlId = urlId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column geturl.url_getname
     *
     * @return the value of geturl.url_getname
     *
     * @mbggenerated
     */
    public String getUrlGetname() {
        return urlGetname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column geturl.url_getname
     *
     * @param urlGetname the value for geturl.url_getname
     *
     * @mbggenerated
     */
    public void setUrlGetname(String urlGetname) {
        this.urlGetname = urlGetname == null ? null : urlGetname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column geturl.url_gettime
     *
     * @return the value of geturl.url_gettime
     *
     * @mbggenerated
     */
    public String getUrlGettime() {
        return urlGettime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column geturl.url_gettime
     *
     * @param urlGettime the value for geturl.url_gettime
     *
     * @mbggenerated
     */
    public void setUrlGettime(String urlGettime) {
        this.urlGettime = urlGettime == null ? null : urlGettime.trim();
    }
}