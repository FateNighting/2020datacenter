package com.datacenter.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
@ToString
/**
 * 内部静态类的单例Config类
 */
public class Config implements Serializable {

    /**
     * 采集网址
     */
    private String configUrl;

    /**
     * 数据源名称
     */
    private String configTitle;
    /**
     * 选择的模型
     */
    private String configModel;

    /**
     * 起始日期
     */
    private String configStart;

    /**
     * 结束日期
     */
    private String configStop;

    /**
     * 采样天数
     */
    private Integer configDays;

    private String configState;

    /**
     * 配置日期
     */
    private String configTime;

    /**
     * 数据总量
     */
    private Integer dataCapability;

    /**
     * 允许采集量
     */
    private Integer dataLimit;

    private Config(){}

    private static class SingerTonHoler{
        private final static Config INSTANCE = new Config();
    }

    public static Config getInstance(){
        return SingerTonHoler.INSTANCE;
    }

    public static void resetConfig(){
        if (SingerTonHoler.INSTANCE != null){
            SingerTonHoler.INSTANCE.
                    setConfigDays(null).
                    setConfigModel(null).
                    setConfigStart(null).
                    setConfigState(null).
                    setConfigStop(null).
                    setConfigTime(null).
                    setConfigUrl(null).
                    setDataCapability(null).
                    setDataLimit(null).
                    setConfigTitle(null);
        }
    }
}