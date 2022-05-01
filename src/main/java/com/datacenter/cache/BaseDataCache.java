package com.datacenter.cache;

import com.datacenter.bean.Countnews;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BaseDataCache{

    public static CopyOnWriteArrayList<Integer> baseDataSizeCache;

    public static CopyOnWriteArrayList<Countnews> testDataCache;

    public static List<Countnews> wellSampleCache;
}
