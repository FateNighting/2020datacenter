package com.datacenter.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WellConfigVo {
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


}
