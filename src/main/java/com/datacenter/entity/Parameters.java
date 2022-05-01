package com.datacenter.entity;

public class Parameters {
    private  String data_set;
    private  String well_size;
    private  String Interval_multiple;
    private  String sampling_rate;
    private  String Abnormal_upper_limit;
    private  String Abnormal_lower_limit;

    public String getData_set() {
        return data_set;
    }

    public void setData_set(String data_set) {
        this.data_set = data_set;
    }

    public String getWell_size() {
        return well_size;
    }

    public void setWell_size(String well_size) {
        this.well_size = well_size;
    }

    public String getInterval_multiple() {
        return Interval_multiple;
    }

    public void setInterval_multiple(String interval_multiple) {
        Interval_multiple = interval_multiple;
    }

    public String getSampling_rate() {
        return sampling_rate;
    }

    public void setSampling_rate(String sampling_rate) {
        this.sampling_rate = sampling_rate;
    }

    public String getAbnormal_upper_limit() {
        return Abnormal_upper_limit;
    }

    public void setAbnormal_upper_limit(String abnormal_upper_limit) {
        Abnormal_upper_limit = abnormal_upper_limit;
    }

    public String getAbnormal_lower_limit() {
        return Abnormal_lower_limit;
    }

    public void setAbnormal_lower_limit(String abnormal_lower_limit) {
        Abnormal_lower_limit = abnormal_lower_limit;
    }
}
