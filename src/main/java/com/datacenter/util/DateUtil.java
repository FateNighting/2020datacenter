package com.datacenter.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    /**
     * 获取某天及后n天的日期list
     */
    public static List<Date> theDayAfterNdays(Date date,int n) {
        ArrayList<Date> futureDaysList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            futureDaysList.add(getFutureDate(date,i));
        }
        return futureDaysList;
    }

    /**
     * 获取未来 第 past 天的日期
     * @param past
     * @return
     */
    public static Date getFutureDate(Date date ,int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,past);
        Date today = calendar.getTime();
        return today;
    }
}
