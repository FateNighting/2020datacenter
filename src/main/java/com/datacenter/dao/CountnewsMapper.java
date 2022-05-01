package com.datacenter.dao;

import com.datacenter.bean.Countnews;
import com.datacenter.bean.CountnewsExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CountnewsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table countnews
     *
     * @mbggenerated
     */
    int countByExample(CountnewsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table countnews
     *
     * @mbggenerated
     */
    int deleteByExample(CountnewsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table countnews
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer countId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table countnews
     *
     * @mbggenerated
     */
    int insert(Countnews record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table countnews
     *
     * @mbggenerated
     */
    int insertSelective(Countnews record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table countnews
     *
     * @mbggenerated
     */
    List<Countnews> selectByExample(CountnewsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table countnews
     *
     * @mbggenerated
     */
    Countnews selectByPrimaryKey(Integer countId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table countnews
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") Countnews record, @Param("example") CountnewsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table countnews
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") Countnews record, @Param("example") CountnewsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table countnews
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Countnews record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table countnews
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Countnews record);
    List<Countnews> selectByCountall(String countAll);
    //int selectByCountsum(int countId);
    //List<Countnews> selectByCountid(int countId);
    
}