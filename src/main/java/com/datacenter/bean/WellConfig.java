package com.datacenter.bean;

import com.datacenter.vo.WellConfigVo;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 采用水井采样的时候存储配置参数，采用单例缓存的方式实现
 */
@Accessors(chain = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class WellConfig implements Serializable {
    //前端输入参数:数据集名称(字符串)，井的大小（WS：int型），间隔倍数（m：float）,采样率（p:float），异常上线(seta1:float), 异常下线(seta2:float)

    /**
     * 数据集名称(字符串)
     */
    private String dataSource;

    /**
     * 井的大小（WS：int型）
     */
    private int wellSize;

    /**
     * 间隔倍数（m：float）
     */
    private double intervalMagnification;

    /**
     * 采样率（p:float
     */
    private double samplingRate;

    /**
     * 异常上线(seta1:float)
     */
    private double abnormalUpperLimit;

    /**
     * 异常下线(seta2:float)
     */
    private double abnormalLowerLimit;

    /**
     * 采样后的数据文件地址
     */
    private String samplingUrl;
    /**
     * 访问率
     */
    private double accessRate;
    /**
     * 访问均值
     */
    private double accessMean;

    private WellConfig(){}

    private static class SingleInstance{
        private final static WellConfig INSTANCE = new WellConfig();
    }

    public static WellConfig getInstance(){
        return SingleInstance.INSTANCE;
    }

    private void init(){
        this.setDataSource(null).setAbnormalLowerLimit(0).setAbnormalUpperLimit(0).
                setWellSize(0).setIntervalMagnification(0).setSamplingRate(0).
                setSamplingUrl(null).setAccessMean(0).setAccessRate(0);
    }

    public void initByVo(WellConfigVo vo){
        init();
        this.dataSource = vo.getDataSource();
        this.abnormalLowerLimit = vo.getAbnormalLowerLimit();
        this.wellSize = vo.getWellSize();
        this.intervalMagnification = vo.getIntervalMagnification();
        this.abnormalUpperLimit = vo.getAbnormalUpperLimit();
        this.samplingRate = vo.getSamplingRate();
    }

}
