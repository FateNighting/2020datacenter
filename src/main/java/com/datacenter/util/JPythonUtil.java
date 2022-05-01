package com.datacenter.util;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JPythonUtil {
    //执行Python文件获得对应的输出
    public static List<String> pyScriptHelper(String[] scriptArgs){
        List res = new ArrayList();
        try {
            Process proc = Runtime.getRuntime().
                exec(scriptArgs);// 执行py文件

            proc.waitFor();
            System.out.println(proc.exitValue());
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                res.add(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Test
    public void test(){
//        "HSI2014",10,0.5,0.02,1.5,0.5 1.5 0.5 44.46 149
        List<String> s = JPythonUtil.pyScriptHelper(new String[]{"python", "/usr/share/pyshared/DSModel/DSModel/EstimateModel.py",
                "HSI2014",
                "1.5","0.5","44.46","149",
                "/usr/share/pyshared/DSModel/DSModel/DataSet/sample.csv"});
        System.out.println(s);
    }


    //调用SampleModel.py文件：输入6个参数，得到sample.csv文件
    //参数为：数据集、井的大小、间隔倍数、采样率、异常上限、异常下限
    public static List<String> getSample(String data_set,String well_size,String Interval_multiple,String sampling_rate,String Abnormal_upper_limit,String Abnormal_lower_limit){
        List<String> s=JPythonUtil.pyScriptHelper(new String[]{"python",
                "/root/data_detection/model/SampleModel.py",
        data_set,well_size,Interval_multiple,sampling_rate,Abnormal_upper_limit,Abnormal_lower_limit});
        System.out.println(s);
        return s;
    }
    //调用EstimateModel.py文件：输入8个参数，得到sampleResult.csv文件
    //参数为：文件路径、访问率、访问均值、数据集、井的大小、间隔倍数、采样率、异常上限、异常下限
    public static void getSampleResult(String data_set,String Abnormal_upper_limit,String Abnormal_lower_limit,String accessRate,String accessMean,String filepath_sample,String well_size,String Interval_multiple,String sampling_rate){
        //s为一个list数组，每一条为其生成CSV文件的一行
        List<String> s=JPythonUtil.pyScriptHelper(new String[]{"python",
                "/root/data_detection/model/EstimateModel.py",
        data_set,Abnormal_upper_limit,Abnormal_lower_limit,accessRate,accessMean,filepath_sample,well_size,Interval_multiple,sampling_rate});
        System.out.println(s);
    }

}

