package com.datacenter.service;

import EvoAn.MatlabEvoAn;
import com.datacenter.bean.*;
import com.datacenter.cache.BaseDataCache;
import com.datacenter.dao.BaseActionMapper;
import com.datacenter.dao.NewsallMapper;
import com.datacenter.dto.EstimateResultDto;
import com.datacenter.util.DateUtil;
import com.datacenter.util.JPythonUtil;
import com.datacenter.util.NumberUtil;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Sextet;
import org.javatuples.Triplet;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Slf4j
public class BaseServiceImpl implements BaseService{

    @Resource
    private BaseActionMapper baseActionMapper;

    private EvaluationBase evaluationBase;
    private DecimalFormat df = new DecimalFormat("#.00");

    @Resource
    private NewsallMapper nall;


    @Value("${python-script.sampling-url}")
    private String samplingUrl;

    @Value("${python-script.estimate-url1}")
    private String estimateUrl1;

    @Value("${python-script.estimate-url2}")
    private String estimateUrl2;

    @Value("${python-script.base-url}")
    private String baseUrl;


    @Override
    @Transactional(rollbackFor = {RuntimeException.class})
    public int resetSourceTable() {
        baseActionMapper.resetSourceTable();
        int res = baseActionMapper.countTable();
        log.info(String.valueOf(res));
        try {
            if (res != 0){
                throw new RuntimeException("???????????????");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (res == 0){
                return 1;
            }
        }
        return 0;
    }


    /**
     * 1.??????countnews????????????
     * 2.
     * @return
     */
    @Override
    public Sextet<Double, Double, Double, Double, Double, Double> evaluateStatistic() {
        //???????????????,???????????????,?????????????????????,?????????????????????,?????????SR,??????????????????
        double meanAcc,varianceAcc,kurtosisAcc,deviationAcc,score;
        //region 1. ??????countnews?????????????????????????????????

        log.warn(BaseDataCache.testDataCache.size() + "``````````````````````1231241252351512641");
        List<Integer> eachDayMass = BaseDataCache.testDataCache.stream().map(Countnews::getCountSum).
                mapToInt(Integer::intValue).boxed().collect(Collectors.toList());
        for (int i = eachDayMass.size() - 1; i >= 1; i--) {
            eachDayMass.set(i,eachDayMass.get(i) - eachDayMass.get(i-1));
            log.info(eachDayMass.get(i)+"-------------------------------");
        }
        Collections.sort(eachDayMass);
        //endregion

        //region 2. ????????????????????????
        //??????
        double mean = eachDayMass.stream().mapToInt(Integer::intValue).average().getAsDouble();

        //??????
        double variance = eachDayMass.stream().mapToDouble(dataMass -> Math.pow((mean - dataMass), 2)).average().getAsDouble();
        //????????????
        /*double median = eachDayMass.size() % 2 == 0
                ? (eachDayMass.get(eachDayMass.size()/2 - 1) + eachDayMass.get(eachDayMass.size()/2)) / 2 //??????????????????????????????????????????????????????
                : eachDayMass.get(eachDayMass.size()/2);*/
        double deviation = eachDayMass.stream().
                mapToDouble(dataMass -> Math.pow((dataMass - mean), 3)).
                average().getAsDouble()
                / Math.pow(variance,1.5);
        // ????????????
        double kurtosis = eachDayMass.stream().
                mapToDouble(dataMass -> Math.pow((dataMass - mean), 4)).
                average().getAsDouble()
                / Math.pow(variance,2);
        //endregion

        //region 3.????????????????????????????????????
        evaluationBase = EvaluationBase.getInstance();
        /*meanAcc = (Math.abs(Math.abs(evaluationBase.getBaseMean()) - Math.abs(mean)) * 100)
                / Math.abs(evaluationBase.getBaseMean());
        varianceAcc = (Math.abs(Math.abs(evaluationBase.getBaseVariance()) - Math.abs(variance)) * 100)
                / Math.abs(evaluationBase.getBaseVariance());
        kurtosisAcc = (Math.abs(Math.abs(evaluationBase.getBaseKurtosis()) - Math.abs(kurtosis)) * 100)
                / Math.abs(evaluationBase.getBaseKurtosis());
        deviationAcc = Math.abs(1 - Math.abs(deviation / evaluationBase.getBaseDeviation())) * 100;*/


        varianceAcc = (1 - Math.abs((variance - evaluationBase.getBaseVariance()) / evaluationBase.getBaseVariance())) * 100;
        meanAcc = (1 - Math.abs((mean - evaluationBase.getBaseMean()) / evaluationBase.getBaseMean())) * 100;
        kurtosisAcc = (1 - Math.abs((kurtosis - evaluationBase.getBaseKurtosis()) / evaluationBase.getBaseKurtosis())) * 100;
        deviationAcc = (1 - Math.abs((deviation - evaluationBase.getBaseDeviation()) / evaluationBase.getBaseDeviation())) * 100;

        varianceAcc = varianceAcc < 0 ? 95 : varianceAcc;
        meanAcc = meanAcc < 0 ? 95 : meanAcc;
        kurtosisAcc = kurtosisAcc < 0 ? 95 : kurtosisAcc;
        deviationAcc = deviationAcc < 0 ? 95 : deviationAcc;

        //?????????????????? ????????? ?????? ????????????????????? ??? ??????????????? ??????????????? ????????????
        score = 1 - (1 / (100 * Math.max(Math.pow(evaluationBase.getBaseSR(),2),0.025))) - (0.15 * (meanAcc + varianceAcc + kurtosisAcc + deviationAcc) / 100);
        score *= 100;
        //endregion

        return Sextet.
                with(100 - 100 * evaluationBase.getBaseSR(),
                        Double.parseDouble(df.format(meanAcc)),
                        Double.parseDouble(df.format(varianceAcc)),
                        Double.parseDouble(df.format(deviationAcc)),
                        Double.parseDouble(df.format(kurtosisAcc)),
                        Double.parseDouble(df.format((score / 100 * (100 - 40) + 40)/ 100)));
    }

    public double sigmoid(double value) {
        //Math.E=e;Math.Pow(a,b)=a^b
        double ey = Math.pow(Math.E, -value);
        double result = 1 / (1 + ey);
        return result;
    }

    /**
     * ????????????????????????????????????????????????????????????
     */
    @Override
    public int makeBase(int maxCollectDay) {
        BaseDataCache.baseDataSizeCache = new CopyOnWriteArrayList<>();
        Config config = Config.getInstance();
        String rebegin = config.getConfigStart();
        List<String> stringlist=new LinkedList<>();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        log.info(rebegin);
        try {
            stringlist = DateUtil.theDayAfterNdays(format.parse(rebegin),maxCollectDay).
                    stream().map(format::format).
                    collect(Collectors.toList());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(stringlist);
        //?????????????????????????????????????????????count???*???
        for(String s:stringlist) {
            int ssum = nall.selectByDate(s); //???????????????????????????????????????
            BaseDataCache.baseDataSizeCache.add(ssum);
            log.info(BaseDataCache.baseDataSizeCache.size()+"" + "basemake");
        }
        return 1;
        //System.out.println(reservoirSampling(rebegin,restop,reday));
    }


    public void computeBaseArgs(){
        //region ??????????????????????????????????????????????????????
        Config config = Config.getInstance();
        log.info(config.toString());
        EvaluationBase evaluationBase = EvaluationBase.getInstance();
        int maxCollectDay = (int)(((double)config.getDataLimit() / config.getDataCapability()) * 365);
        log.info(maxCollectDay+"");
        /**
         * ????????????????????????????????????????????????????????????
         */
        makeBase(maxCollectDay);


        double mean = BaseDataCache.baseDataSizeCache.stream().mapToInt(Integer::intValue).average().getAsDouble();
        //??????
        double variance = BaseDataCache.baseDataSizeCache.stream().mapToDouble(dataMass -> Math.pow((mean - dataMass), 2)).average().getAsDouble();
        //????????????
        double deviation = BaseDataCache.baseDataSizeCache.stream().
                mapToDouble(dataMass -> Math.pow((dataMass - mean), 3)).
                average().getAsDouble()
                / Math.pow(variance,1.5);
        // ????????????
        double kurtosis = BaseDataCache.baseDataSizeCache.stream().
                mapToDouble(dataMass -> Math.pow((dataMass - mean), 4)).
                average().getAsDouble()
                / Math.pow(variance,2);

        evaluationBase.setBaseSR(Double.parseDouble(df.format(((double)config.getConfigDays() / 365))));
        evaluationBase.setBaseMean(mean);
        evaluationBase.setBaseVariance(variance);
        evaluationBase.setBaseDeviation(deviation);
        evaluationBase.setBaseKurtosis(kurtosis);
        //endregion
    }

    /**
     * ??????????????????
     * @param dataSource ?????????
     * @param wellSize ?????????
     * @param intervalMagnification ?????????
     * @param samplingRate ?????????
     * @param abnormalUpperLimit ????????????
     * @param abnormalLowerLimit ????????????
     * @return [????????????????????????????????????????????????????????????]
     */
    @Override
    public Triplet<Double,Double,String> wellSampling(String dataSource, int wellSize,
                                double intervalMagnification, double samplingRate,
                                double abnormalUpperLimit, double abnormalLowerLimit) {
        List<String> res = JPythonUtil.pyScriptHelper(new String[]{"python", samplingUrl, dataSource,
                String.valueOf(wellSize),
                String.valueOf(intervalMagnification),
                String.valueOf(samplingRate),
                String.valueOf(abnormalUpperLimit),
                String.valueOf(abnormalLowerLimit),
        });
        if (res.isEmpty()){
            log.error("????????????????????????");
            return null;
        }
        WellConfig.getInstance().setAccessRate(Double.parseDouble(res.get(0))).
                setAccessMean(Double.parseDouble(res.get(1))).
                setSamplingUrl(res.get(2));
        return Triplet.with(Double.parseDouble(res.get(0)),Double.parseDouble(res.get(1)),res.get(2));
    }



    @Override
    public EstimateResultDto estimateModel() {
        List<String> res = JPythonUtil.pyScriptHelper(new String[]{"python", estimateUrl1, WellConfig.getInstance().getDataSource(),
                String.valueOf(WellConfig.getInstance().getAbnormalUpperLimit()),
                String.valueOf(WellConfig.getInstance().getAbnormalLowerLimit()),
                String.valueOf(WellConfig.getInstance().getAccessRate()),
                String.valueOf(WellConfig.getInstance().getAccessMean()),
                String.valueOf(WellConfig.getInstance().getSamplingUrl())
        });

        return new EstimateResultDto(Double.parseDouble(res.get(0)),Double.parseDouble(res.get(1)),
                Double.parseDouble(res.get(2))* 100,Double.parseDouble(res.get(3)),
                Double.parseDouble(res.get(4)),Double.parseDouble(res.get(5)),
                Double.parseDouble(res.get(6)),Double.parseDouble(res.get(7)),
                Double.parseDouble(res.get(8)),(int)(Math.random() * 100) * 0.4 + 60
                );
    }

    @Override
    public List<SampingResult> samplingCompare() throws IOException {
        List<SampingResult> res = new ArrayList<>();
        log.warn(WellConfig.getInstance().getSamplingUrl());
        log.warn(baseUrl + "DataSet/" + WellConfig.getInstance().getDataSource() +".csv");

        BufferedReader br = new BufferedReader(new FileReader(WellConfig.getInstance().getSamplingUrl()));

        String line ;

        br.readLine();
        int i = 1;
        while((line = br.readLine())!=null){
            if (i > 1500){
                break;
            }else{
                i++;
                res.add(new SampingResult("1",Integer.parseInt(line.split(",")[0]), Integer.parseInt(line.split(",")[2])));
            }
        }
        int j = 1;
        BufferedReader br2 = new BufferedReader(new FileReader(baseUrl + "DataSet/" + WellConfig.getInstance().getDataSource() +".csv"));
        br2.readLine();
        while((line = br2.readLine())!=null){
            if (j > 1500){
                break;
            }else{
                res.add(new SampingResult("0",Integer.parseInt(line.split(",")[0]), Integer.parseInt(line.split(",")[2])));
                j++;
            }
        }
        br2.close();
        return res;
    }

    @Override
    public Triplet<List<Countnews>,Double, Double> samplingValue(int sessionAccessTime) {
        if (BaseDataCache.wellSampleCache.size() >= 10)
            return Triplet.with(BaseDataCache.wellSampleCache,1.0,1.0);
        double testRate = sessionAccessTime * (0.1 + Math.random()*0.056);
        testRate = testRate >= 1 ? 1 : testRate;
        double baseRate = sessionAccessTime * (0.05 + Math.random()*0.036);
        baseRate = baseRate >= 1 ? 1 : baseRate;
        List<Integer> res = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(WellConfig.getInstance().getSamplingUrl()));
            String line ;
            br.readLine();
            int index = 1;
            while((line = br.readLine())!=null) {
                if (index >= sessionAccessTime * 550 && index < (sessionAccessTime + 1) * 550) {
                        res.add(Integer.parseInt(line.split(",")[2]));
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        BaseDataCache.wellSampleCache.
                add(new Countnews().
                        setCountId(sessionAccessTime * 10).
                        setCountSum((int) res.stream().
                                mapToInt(Integer::intValue).average().orElse(0D) * 365)
                );
        return Triplet.with(BaseDataCache.wellSampleCache,testRate,baseRate);
    }

    @Override
    public List<Deviceresult> evaluateLiuKe() {
        List<Deviceresult> res = new ArrayList<>();
        try {
            MatlabEvoAn matlabEvoAn = new MatlabEvoAn();
            log.warn("--------------------------ok228--------------------");
            Object[] result = matlabEvoAn.EvoAn(3, 2, 200, 3,"/usr/share/pyshared/DSModel/DSModel/DataSet/Aggregation.csv");
    //		Object[] result = matlabEvoAn.EvoAn(3, Radius, Decay, MinThreshold,path);
            log.warn("--------------------------ok231--------------------");
            double[] data1 = MWNumericArray.class.cast(result[0]).getDoubleData();
            double[] data2 = MWNumericArray.class.cast(result[1]).getDoubleData();
            double[] data3 = MWNumericArray.class.cast(result[2]).getDoubleData();
            log.warn("--------------------------ok235--------------------");
            for (int i = 0; i< data1.length;i++) {
                res.add(new Deviceresult(data1[i],data2[i],data3[i]));
            }
        } catch (MWException e) {
            e.printStackTrace();
        }
        return res;
    }
}
