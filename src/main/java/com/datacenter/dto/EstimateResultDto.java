package com.datacenter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EstimateResultDto {
    /**
     * 访问率
     */
    private double accessRate;
    /**
     * 采样率
     */
    private double sampleRate;
    private double ACR;
    private double JSD;
    private double PTMeanPrecision;
    private double PTCVPrecision;
    private double MPTMeanPrecision;
    private double MPTCVPrecision;
    private double precision;

    private double score;
}
