package com.datacenter.service;

import com.datacenter.bean.Countnews;
import com.datacenter.bean.Deviceresult;
import com.datacenter.bean.SampingResult;
import com.datacenter.dto.EstimateResultDto;
import org.javatuples.Sextet;
import org.javatuples.Triplet;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface BaseService {

    /**
     * 清空表
     * @return [1表示清空成功；0表示运行错误；-1表示清空有残留]
     */
    int resetSourceTable();

    /**
     * 获取5大指标以及最终评估分数
     *
     * @return Sextet<Double,Double,Double,Double,Double,Double>[均值准确率,方差准确率,偏态系数准确率,峰态系数准确率,采样率SR,最终评估分数]
     */
    Sextet<Double,Double,Double,Double,Double,Double> evaluateStatistic();

    int makeBase(int maxCollectDay);

    /**
     * 计算基准参数值
     */
    void computeBaseArgs();


    /**
     * 水井采样
     */
    Triplet<Double,Double,String> wellSampling(String dataSource, int wellSize, double intervalMagnification, double samplingRate,
                                               double abnormalUpperLimit, double abnormalLowerLimit);
    /**
     * 模型评估
     */
    EstimateResultDto estimateModel();

    List<SampingResult> samplingCompare() throws IOException;

    Triplet<List<Countnews>,Double, Double> samplingValue(int sessionAccessTime);

    List<Deviceresult> evaluateLiuKe();
}
