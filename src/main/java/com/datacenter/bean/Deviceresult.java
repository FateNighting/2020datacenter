package com.datacenter.bean;
public class Deviceresult {
    private Integer id;

    /**
     * 中心
     */
    private Double coreclassC;

    /**
     * 类包含的样本量
     */
    private Double coreclassN;

    /**
     * 密度
     */
    private Double coreclassD;

    public Deviceresult(Double coreclassC, Double coreclassN, Double coreclassD) {
        this.coreclassC = coreclassC;
        this.coreclassN = coreclassN;
        this.coreclassD = coreclassD;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getCoreclassC() {
        return coreclassC;
    }

    public void setCoreclassC(Double coreclassC) {
        this.coreclassC = coreclassC;
    }

    public Double getCoreclassN() {
        return coreclassN;
    }

    public void setCoreclassN(Double coreclassN) {
        this.coreclassN = coreclassN;
    }

    public Double getCoreclassD() {
        return coreclassD;
    }

    public void setCoreclassD(Double coreclassD) {
        this.coreclassD = coreclassD;
    }

    @Override
    public String toString() {
        return "Deviceresult{" +
                "id=" + id +
                ", coreclassC=" + coreclassC +
                ", coreclassN=" + coreclassN +
                ", coreclassD=" + coreclassD +
                '}';
    }

}