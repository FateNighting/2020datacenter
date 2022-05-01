package com.datacenter.dao;

import com.datacenter.bean.Countnews;
import com.datacenter.bean.Newsall;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseActionMapper {

    int resetSourceTable();

    /**
     * 获取表中记录数量
     * @return
     */
    int countTable();

    /**
     * 获取表中所有内容
     * @return
     */
    List<Countnews> selectAllFromCountNews();
}
