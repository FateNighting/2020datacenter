package com.datacenter.service;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ModelServiceImpl implements ModelService {

	//模型1：随机生成指定天数的日期
	@Override
	public void receiveModelOne(Statics statics, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
			
			
		String reurl=request.getParameter("reUrl");
		String remodel =request.getParameter("reSamplingmodel");
		String rebegin=request.getParameter("reBegin");
		String restop=request.getParameter("reStop");
		int reday=Integer.parseInt(request.getParameter("reDay"));
	}
			
			

	

	@Override
	public JSONObject getApplyType() throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getSampling() throws JSONException {
		// TODO Auto-generated method stub
		return null;
	}
	//随机生成日期
	private static Date randomDate(String beginDate,String endDate){
	        try {
	            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	            Date start = format.parse(beginDate);
	            Date end = format.parse(endDate);
	 
	            if(start.getTime() >= end.getTime()){
	                return null;
	            }
	            long date = random(start.getTime(),end.getTime());
	            return new Date(date);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	 //随机生成日期
	 private static long random(long begin,long end){
	        long rtn = begin + (long)(Math.random() * (end - begin));
	        if(rtn == begin || rtn == end){
	            return random(begin,end);
	        }
	        return rtn;
	    }

}
