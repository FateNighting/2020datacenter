package com.datacenter.util;

import org.springframework.beans.factory.annotation.Value;

import java.text.NumberFormat;
import java.util.List;

public class NumberUtil {

    private final static NumberFormat nf = NumberFormat.getPercentInstance();

    private static int INTEGER_DIGIT;
    private  static int FRACTION_DIGITS;

    @Value("${number.util.IntegerDigits}")
    private void setIntegerDigits(int i){
        this.INTEGER_DIGIT = i;
    }

    @Value("${number.util.IntegerDigits}")
    private void setFractionDigits(int i){
        this.FRACTION_DIGITS = i;
    }
    public NumberUtil(){
        System.out.println(123412);


    }
    /**
     * double类型转百分数
     * @param d
     * @return
     */
    public static String getPercentFormat(double d){
        nf.setMaximumIntegerDigits(INTEGER_DIGIT);
        nf.setMinimumFractionDigits(FRACTION_DIGITS);
        return nf.format(d);
    }

}
