package com.datacenter.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
public class EvaluationBase {
    private double baseMean;
    private double baseVariance;
    private double baseKurtosis;
    private double baseDeviation;
    private double baseSR;
    private EvaluationBase(){}
    private volatile static EvaluationBase INSTANCE;
    public static EvaluationBase getInstance(){
        if (INSTANCE == null) {
            synchronized (EvaluationBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EvaluationBase();
                }
            }
        }
        return INSTANCE;
    }
}
